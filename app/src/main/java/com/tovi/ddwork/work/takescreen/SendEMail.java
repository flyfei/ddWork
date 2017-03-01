package com.tovi.ddwork.work.takescreen;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.tovi.ddwork.Config;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

/**
 * @author <a href='mailto:zhaotengfei9@gmail.com'>Tengfei Zhao</a>
 */

public class SendEMail {

    public static void send(String fileName, String subject, OnSendListener onSendListener) {
        String[] recipients = {Config.TO};
        SendEmailAsyncTask email = new SendEmailAsyncTask();
        email.m = new Mail(Config.FROM, Config.PASS);
        email.m.set_from(Config.FROM);
        email.m.set_to(recipients);
        email.m.set_subject(subject);
        
        if (TextUtils.isEmpty(fileName)) {
            email.m.setBody("通知类消息");
        } else {
            email.m.setBody("操作结果，见附件");
            try {
                email.m.addAttachment(fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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