package com.adongs.session.aggregator;

import com.adongs.config.AuthenticationConfig;
import com.adongs.constant.Logical;
import com.adongs.exception.WrongFormatException;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文件解析,支持ant风格匹配url和权限,角色,可以使用*进行匹配
 * 文件内容保持如下格式
 * /url=role[角色1,角色2]authority[权限1,权限2]
 * 例如
 * /test/user/name=role[user,admin]authority[select,delete,update,insatll]
 * @author yudong
 * @version 1.0
 */
@Component
public class FilesAuthority extends CollectionAuthortiy {
    private final static Log LOGGER = LogFactory.getLog(FilesAuthority.class);
    private  final static Pattern  PATTERN = Pattern.compile("/[A-z0-9\\*.-/]*=(anon|((role(\\((or|and)?\\))?\\[[\\w,]*\\])*(authority(\\((or|and)?\\))?\\[[\\w,]*\\])*))");
    private final static String  ROLE_PREFIX="role";
    private final static String  ANON_PREFIX="anon";
    private final static String  AUTHORITY_PREFIX="authority";
    private final static String  CONTENT_PREFIX="[";
    private final static String  CONTENT_SUFFIX="]";
    private final static String  LOGICAL_PREFIX="(";
    private final static String  LOGICAL_SUFFIX=")";
    private final static String  LIMIT="=";
    private final static String  DELIMITER=",";

    public FilesAuthority(AuthenticationConfig config) {
        super(config);
    }


    @Override
    public Map<String, UrlAuthority> collection() {
         AuthenticationConfig config = config();
        final String[] authorityFile = config.getAuthorityFile();
        if (authorityFile==null || authorityFile.length==0){
            return null;
        }
        Map<String, UrlAuthority> read = read(config.getAuthorityFile());
        return read;
    }

    /**
     * 读取所有文件内容
     * @param paths 文件路径集
     * @return 解析后的文件内容
     */
    public Map<String, UrlAuthority> read(String [] paths){
        Map<String, UrlAuthority> map = Maps.newHashMap();
        for (int i = 0,l=paths.length; i < l; i++) {
            map.putAll(read(paths[i]));
        }
     return map;
    }

    /**
     * 读取文件内容
     * @param path 文件路径
     * @return 解析后的文件内容
     */
    public Map<String, UrlAuthority> read(String path){
        Map<String, UrlAuthority> map = Maps.newHashMap();
        Resource classPathResource = new ClassPathResource(path);
        boolean exists = classPathResource.exists();
        if (!exists){
            throw new NullPointerException("file path is null path:"+path);
        }
        try( FileReader fr = new FileReader(classPathResource.getFile());
            BufferedReader bf = new BufferedReader(fr)){
            for (String str;(str = bf.readLine())!=null;){
                final Matcher matcher = PATTERN.matcher(str);
                if (!matcher.matches()){
                 throw new WrongFormatException("权限规则不匹配",path,str);
                }
                parsing(str,map);
            }
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage(),e);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(),e);
        }
           return map;
    }


    /**
     * 数据解析
      * @param data 数据
     * @return 解析后的数据
     */
  private void parsing(String data,Map<String, UrlAuthority> map){
      final int i = data.indexOf(LIMIT);
      String url = data.substring(0,i);
      final int anonIndex = data.indexOf(ANON_PREFIX);
      if (anonIndex!=-1){
          map.put(url,new UrlAuthority(true));
          return;
      }
      Set<String> roles = extract(data,data.indexOf(ROLE_PREFIX));
      final Logical rolesLogical = relationship(data, data.indexOf(ROLE_PREFIX));
      Set<String> authoritys = extract(data,data.indexOf(AUTHORITY_PREFIX));
      final Logical authoritysLogical = relationship(data, data.indexOf(AUTHORITY_PREFIX));
      map.put(url,new UrlAuthority(roles,rolesLogical,authoritys,authoritysLogical));
  }

    /**
     * 内容提取
     * @param data 数据
     * @param index 起点索引
     * @return 提取的数据
     */
    private Set<String> extract(String data,int index){
        if (index!=-1){
            final int i1 = data.indexOf(CONTENT_PREFIX, index);
            final int i2 = data.indexOf(CONTENT_SUFFIX, i1);
            final String substring = data.substring(i1+1, i2);
            return Sets.newHashSet(substring.split(DELIMITER));
        }
        return Sets.newHashSet();
    }

    /**
     * 提取关系
     * @param data 数据
     * @param index 起点索引
     * @return 关系
     */
    private Logical relationship(String data,int index){
        if (index!=-1){
            final int i1 = data.indexOf(LOGICAL_PREFIX, index);
            final int i2 = data.indexOf(LOGICAL_SUFFIX, i1);
            if (i1!=-1 && i2!=-1) {
                final String substring = data.substring(i1 + 1, i2);
                if (substring.equals(Logical.AND.name().toLowerCase())) {
                    return Logical.AND;
                }
            }
        }
        return Logical.OR;
    }

}
