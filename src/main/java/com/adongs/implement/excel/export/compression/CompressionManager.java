package com.adongs.implement.excel.export.compression;

import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 压缩管理器
 * @author yudong
 * @version 1.0
 */
@Component
public class CompressionManager {

    private final Map<String,Compression> COMPRESSION_MAP = Maps.newHashMap();

    public CompressionManager(Compression ... compressions) {
        for (Compression compression : compressions) {
            COMPRESSION_MAP.put(compression.compressType(),compression);
        }
    }

    public CompressionManager() {
        this(new ZipCompression());
    }

    /**
     * 添加压缩处理器
     * @param compression 压缩处理器
     * @return 压缩管理器
     */
    public CompressionManager add(Compression compression){
        boolean containsKey = COMPRESSION_MAP.containsKey(compression.compressType());
        if (containsKey){
            throw new RuntimeException(compression.compressType()+" repeat");
        }
        COMPRESSION_MAP.put(compression.compressType(),compression);
        return this;
    }

    /**
     * 获取压缩处理器
     * @param type 类型
     * @return 压缩处理器
     */
    public Compression getType(String type){
        Compression compression = COMPRESSION_MAP.get(type);
        if (compression==null){
            throw new NullPointerException(type+" Compression is null");
        }
        return compression;
    }

}
