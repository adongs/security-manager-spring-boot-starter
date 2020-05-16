package com.adongs.constant;

/**
 * @author yudong
 * @version 1.0
 */
public enum  ResubmitTime {
    UNLIMITED{
        @Override
        public ResubmitTime time(long time) {

            return ResubmitTime.UNLIMITED;
        }
    },
    CONFIG{
        @Override
        public ResubmitTime time(long time) {
            return ResubmitTime.CONFIG;
        }
    },
    CUSTOMIZE{
        @Override
       public ResubmitTime time(long time) {
            final ResubmitTime customize = ResubmitTime.CUSTOMIZE;
            customize.setTimes(time);
            return customize;
        }
    };

    private  long times;


   public abstract ResubmitTime time(long time);

    private void setTimes(long times) {
        this.times = times;
    }

    public long getTimes() {
        return times;
    }
}
