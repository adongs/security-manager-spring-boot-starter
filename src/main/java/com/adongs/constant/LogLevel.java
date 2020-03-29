package com.adongs.constant;

import org.apache.commons.logging.Log;
/**
 * @author adong
 * @version 1.0
 */
public enum LogLevel implements Out {


    FATAL(){
        @Override
        public void out(Log log,String outlog) {
          if(log.isFatalEnabled())log.fatal(outlog);
        }

        @Override
        public boolean isOut(Log log) {
            return log.isFatalEnabled();
        }
    },
    ERROR(){
        @Override
        public void out(Log log,String outlog) {
            if(log.isErrorEnabled())log.error(outlog);
        }

        @Override
        public boolean isOut(Log log) {
            return log.isErrorEnabled();
        }
    },WARN(){
        @Override
        public void out(Log log,String outlog) {
            if(log.isWarnEnabled())log.warn(outlog);
        }
        @Override
        public boolean isOut(Log log) {
            return log.isWarnEnabled();
        }
    },INFO(){
        @Override
        public void out(Log log,String outlog) {
            if(log.isInfoEnabled())log.info(outlog);
        }
        @Override
        public boolean isOut(Log log) {
            return log.isInfoEnabled();
        }
    },DEBUG(){
        @Override
        public void out(Log log,String outlog) {
            if(log.isDebugEnabled())log.debug(outlog);
        }
        @Override
        public boolean isOut(Log log) {
            return log.isDebugEnabled();
        }
    },TRACE(){
        @Override
        public void out(Log log,String outlog) {
            if(log.isTraceEnabled())log.trace(outlog);
        }
        @Override
        public boolean isOut(Log log) {
            return log.isTraceEnabled();
        }
    };






}

interface Out{
    public void out(Log log,String outlog);

    public boolean isOut(Log log);
}
