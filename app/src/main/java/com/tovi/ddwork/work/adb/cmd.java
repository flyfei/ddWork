package com.tovi.ddwork.work.adb;

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
    public static void offWork(boolean isLeaveEarly, int[] location, OnOKListener onOKListener) {
        if (location == null || location.length < 2) return;

        randomSleep(10);
        execShellCmd("adb shell");
        // into
        execShellCmd(String.format("input tap %s %s", location[0], location[1]));
        sleep(10);
        // offWork
        execShellCmd("input tap 537 1397");
        sleep(5);
        if (isLeaveEarly) {
            execShellCmd("input tap 766 1514");
            sleep(5);
        }
        if (onOKListener != null) onOKListener.onOk("offWork");
        execShellCmd("input keyevent KEYCODE_BACK");
        sleep(5);
        if (onOKListener != null) onOKListener.onGotoHome();
    }

    /**
     * onWork
     */
    public static void onWork(int[] location, OnOKListener onOKListener) {
        if (location == null || location.length < 2) return;

        randomSleep(10);
        execShellCmd("adb shell");
        // into
        execShellCmd(String.format("input tap %s %s", location[0], location[1]));
        sleep(10);
        // onWork
        execShellCmd("input tap 540 1015");
        sleep(5);
        if (onOKListener != null) onOKListener.onOk("onWork");
        execShellCmd("input keyevent KEYCODE_BACK");
        sleep(5);
        if (onOKListener != null) onOKListener.onGotoHome();
    }

    public static void takeScreen(String filePath) {
        execShellCmd("screencap -p " + filePath);
        sleep(2);
    }

    public static void delFile(String filePath) {
        execShellCmd("rm " + filePath);
    }

    /**
     * for test su
     *
     * @param filePath
     * @return
     */
    public static boolean test(String filePath) {
        boolean res = execShellCmd("screencap -p " + filePath);
        sleep(2);
        return res;
    }


    /**
     * 执行shell命令
     *
     * @param cmd
     */
    private static boolean execShellCmd(String cmd) {

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
            return true;
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
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

    public interface OnOKListener {
        void onOk(String type);

        void onGotoHome();
    }
}
