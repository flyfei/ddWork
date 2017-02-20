package com.tovi.ddwork;

/**
 * @author <a href='mailto:zhaotengfei9@gmail.com'>Tengfei Zhao</a>
 */

public class Config {
    public static final int ON_WORK_HOUR = 10;
    public static final int OFF_WORK_HOUR = 19;

    public static final boolean AUTO_WORK = true;
    public static final int AUTO_ON_WORK_HOUR = 9;
    public static final int AUTO_ON_WORK_MINUTE = 30;
    public static final int AUTO_OFF_WORK_HOUR = 19;
    public static final int AUTO_OFF_WORK_MINUTE = 10;

    public static final boolean AUTO_SEND_EMAIL = true;
    public static final String FROM = "example@sina.com";
    public static final String PASS = "pass";
    public static final String TO = FROM; // 这里发给自己，可以发给其他账户
}
