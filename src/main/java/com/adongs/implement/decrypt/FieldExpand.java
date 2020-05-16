package com.adongs.implement.decrypt;

import com.adongs.annotation.extend.encryption.Cipher;
import com.adongs.annotation.extend.encryption.Decode;
import com.adongs.annotation.extend.encryption.Decrypt;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * 字段扩展信息
 * @author yudong
 * @version 1.0
 */
public class FieldExpand {
    private final Field field;
    private final Expand expand;

    public FieldExpand(Field field, Expand expand) {
        this.field = field;
        this.expand = expand;
    }

    public Field getField() {
        return field;
    }

    public Expand getExpand() {
        return expand;
    }

    public static abstract class Expand{
        private final Class<?> clazz;
        public Expand(Class<?> clazz) {
            this.clazz = clazz;
        }
        public abstract String type();
        public abstract String processor();
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
    public static class Decrypte extends Expand{
        private final Decrypt decrypt;

        public Decrypte(Class<?> clazz, Decrypt decrypt) {
            super(clazz);
            this.decrypt = decrypt;
        }

        @Override
        public String type() {
            return decrypt.value();
        }

        @Override
        public String processor() {
            return decrypt.processor();
        }
    }
    public static class Decodee extends Expand{
        private final Decode decode;

        public Decodee(Class<?> clazz, Decode decode) {
            super(clazz);
            this.decode = decode;
        }

        @Override
        public String type() {
            return decode.value();
        }

        @Override
        public String processor() {
            return decode.processor();
        }
    }
    public static class Ciphere extends Expand{
        private final Cipher cipher;

        public Ciphere(Class<?> clazz, Cipher cipher) {
            super(clazz);
            this.cipher = cipher;
        }

        @Override
        public String type() {
            return null;
        }

        @Override
        public String processor() {
            return null;
        }
    }

}
