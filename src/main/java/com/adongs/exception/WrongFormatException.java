package com.adongs.exception;

/**
 * @author yudong
 * @version 1.0
 */
public class WrongFormatException extends RuntimeException{

    private String fileName;

    private String line;

    public WrongFormatException(String message, String fileName, String line) {
        super(message+" fileName:"+fileName+"  line:"+line);
        this.fileName = fileName;
        this.line = line;
    }

    public String getFileName() {
        return fileName;
    }

    public String getLine() {
        return line;
    }
}
