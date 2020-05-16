package com.adongs.implement.decrypt;

import com.adongs.annotation.extend.encryption.Cipher;
import com.adongs.annotation.extend.encryption.Decode;
import com.adongs.annotation.extend.encryption.Decrypt;
import com.adongs.annotation.extend.encryption.ReturnCipher;

import java.util.Collection;
import java.util.List;

/**
 * 方法信息包装
 * @author yudong
 * @version 1.0
 */
public class MethodExpand {
    /**
     * 方法参数
     */
    private final List<Param> params;
    /**
     * 方法返回值
     */
    private final Return aReturn;


    public MethodExpand(List<Param> params, Return aReturn) {
        this.params = params;
        this.aReturn = aReturn;
    }

    public List<Param> getParams() {
        return params;
    }

    public Return getaReturn() {
        return aReturn;
    }

    public static abstract class Param{
       private final String name;
       private final Class<?> clazz;

        public Param(String name, Class<?> clazz) {
            this.name = name;
            this.clazz = clazz;
        }

        public String getName() {
           return name;
       }

        public Class<?> getClazz() {
            return clazz;
        }
        public boolean isCollection(){
            return  clazz.getClass().isInstance(Collection.class);
        }
        public boolean isString(){
            return  clazz.getClass().isInstance(String.class);
        }
        public boolean isCustomize(){
           return clazz.getClassLoader()!=null;
        }
    }

    public static class Cipherp extends Param{
       private final Cipher cipher;

        public Cipherp(String name, Class<?> clazz, Cipher cipher) {
            super(name, clazz);
            this.cipher = cipher;
        }

        public Cipher getCipher() {
           return cipher;
       }
   }

    public static class Decodep extends Param{
        private final Decode decode;

        public Decodep(String name, Class<?> clazz, Decode decode) {
            super(name, clazz);
            this.decode = decode;
        }

        public Decode getDecode() {
            return decode;
        }
    }

    public static class Decryptp extends Param{
        private final Decrypt decrypt;

        public Decryptp(String name, Class<?> clazz, Decrypt decrypt) {
            super(name, clazz);
            this.decrypt = decrypt;
        }

        public Decrypt getDecrypt() {
            return decrypt;
        }
    }

    public static class Return{
        private final Class<?> clazz;
        private final ReturnCipher returnCipher;

        public Return(Class<?> clazz, ReturnCipher returnCipher) {
            this.clazz = clazz;
            this.returnCipher = returnCipher;
        }

        public Class<?> getClazz() {
            return clazz;
        }

        public ReturnCipher getReturnCipher() {
            return returnCipher;
        }
        public boolean isCollection(){
            return  Collection.class.isAssignableFrom(clazz);
        }
        public boolean isString(){
            return  String.class.isAssignableFrom(clazz);
        }
    }

}
