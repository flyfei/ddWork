package com.tovi.ddwork.work.takescreen;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.tovi.ddwork.Config;

import java.util.ArrayList;
import java.util.List;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

/**
 * @author <a href='mailto:zhaotengfei9@gmail.com'>Tengfei Zhao</a>
 */

public class SendEMail {
    public static void send(String subject, String body, OnSendListener onSendListener) {
        send((List<String>) null, subject, body, onSendListener);
    }

    public static void send(String filename, String subject, String body, OnSendListener onSendListener) {
        List<String> list = new ArrayList<>();
        if (!TextUtils.isEmpty(filename)) list.add(filename);
        send(list, subject, body, onSendListener);
    }

    public static void send(List<String> fileNames, String subject, String body, OnSendListener onSendListener) {
        String[] recipients = {Config.TO};
        SendEmailAsyncTask email = new SendEmailAsyncTask();
        email.m = new Mail(Config.FROM, Config.PASS);
        email.m.set_from(Config.FROM);
        email.m.set_to(recipients);
        email.m.set_subject(subject);

        StringBuffer stringBuffer = new StringBuffer("详情：");
        if (!TextUtils.isEmpty(body)) {
            stringBuffer.append("\n").append(body);
        }
        if (fileNames != null && !fileNames.isEmpty()) {
            stringBuffer.append("\n含附件");
            for (String fileName : fileNames) {
                if (!TextUtils.isEmpty(fileName)) {
                    try {
                        email.m.addAttachment(fileName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        email.m.setBody(stringBuffer.toString());

        email.m.set_host("smtp.sina.com.cn");
        email.m.set_port("110");
        email.onSendListener = onSendListener;
        email.execute();
    }

    public interface OnSendListener {
        void onSendOk();

        void onSendError();
    }
}

class SendEmailAsyncTask extends AsyncTask<Void, Void, Boolean> {
    Mail m;
    SendEMail.OnSendListener onSendListener;

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            if (m.send()) {
                System.out.println("Email sent." + m.get_subject());
                if (onSendListener != null) onSendListener.onSendOk();
            } else {
                System.out.println("Email failed to send." + m.get_subject());
                if (onSendListener != null) onSendListener.onSendError();
            }

            return true;
        } catch (AuthenticationFailedException e) {
            e.printStackTrace();
            System.out.println("Authentication failed.");
            if (onSendListener != null) onSendListener.onSendError();
            return false;
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Email failed to send.");
            if (onSendListener != null) onSendListener.onSendError();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Unexpected error occured.");
            if (onSendListener != null) onSendListener.onSendError();
            return false;
        }
    }
}