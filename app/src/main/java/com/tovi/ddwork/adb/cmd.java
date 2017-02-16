package com.tovi.ddwork.adb;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.Random;

/**
 * @author <a href='mailto:zhaotengfei9@gmail.com'>Tengfei Zhao</a>
 */

public class cmd {
    /**
     * off Work
     *
     * @param isLeaveEarly 是否早退
     */
    public static void offWork(boolean isLeaveEarly) {
        randomSleep(10);
        execShellCmd("adb shell");
        // into
        execShellCmd("input tap 137 1320");
        sleep(10);
        // offWork
        execShellCmd("input tap 537 1397");
        sleep(5);
        if (isLeaveEarly) {
            execShellCmd("input tap 766 1514");
            sleep(5);
        }
        execShellCmd("input keyevent KEYCODE_BACK");
    }

    /**
     * onWork
     */
    public static void onWork() {
        randomSleep(10);
        execShellCmd("adb shell");
        // into
        execShellCmd("input tap 137 1320");
        sleep(10);
        // onWork
        execShellCmd("input tap 540 1015");
        sleep(5);
        execShellCmd("input keyevent KEYCODE_BACK");
    }

    /**
     * 执行shell命令
     *
     * @param cmd
     */
    private static void execShellCmd(String cmd) {

        try {
            // 申请获取root权限，这一步很重要，不然会没有作用
            Process process = Runtime.getRuntime().exec("su");
            // 获取输出流
            OutputStream outputStream = process.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(
                    outputStream);
            dataOutputStream.writeBytes(cmd);
            dataOutputStream.flush();
            dataOutputStream.close();
            outputStream.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * 休眠。单位S
     *
     * @param second
     */
    private static void sleep(long second) {
        System.out.println("second: " + second);
        if (second <= 0) return;
        try {
            Thread.sleep(second * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 随机休眠。单位S
     *
     * @param second
     */
    private static void randomSleep(int second) {
        sleep(new Random().nextInt(second));
    }
}
