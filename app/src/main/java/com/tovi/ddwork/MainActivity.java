package com.tovi.ddwork;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.tovi.ddwork.service.Service;
import com.tovi.ddwork.work.Synchronization;
import com.tovi.ddwork.work.test.Test;

public class MainActivity extends AppCompatActivity {

    private Switch state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        state = (Switch) findViewById(R.id.state);
        state.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // 开启服务
                    Service.start(MainActivity.this, 1 * 60);
                } else {
                    Service.stop(MainActivity.this);
                }
            }
        });

        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Test.start(new Test.OnTestListener() {
                    @Override
                    public void onTestRes(final String res) {

                        final boolean isOk = TextUtils.isEmpty(res);
                        Util.setTested(MainActivity.this, isOk);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, isOk ? "测试成功" : res, Toast.LENGTH_SHORT).show();
                                state.setEnabled(isOk);
                            }
                        });
                    }
                });
            }
        });
        findViewById(R.id.btn_sync).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Synchronization.sync(MainActivity.this);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        // 更新服务状态
        updateServiceStatus();
        // 更新测试状态
        state.setEnabled(Util.getTested());
    }

    /**
     * 更新当前 WorkService 显示状态
     */
    private void updateServiceStatus() {
        state.setChecked(Service.isRunning(this));
    }
}
