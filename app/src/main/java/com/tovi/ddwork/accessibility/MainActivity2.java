package com.tovi.ddwork.accessibility;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Switch;
import android.widget.Toast;

import com.tovi.ddwork.R;

import java.util.List;

// TODO 待开发
public class MainActivity2 extends AppCompatActivity implements AccessibilityManager.AccessibilityStateChangeListener {

    Switch state;
    //AccessibilityService 管理
    private AccessibilityManager accessibilityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        state = (Switch) findViewById(R.id.state);

        //监听AccessibilityService 变化
        accessibilityManager = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
        accessibilityManager.addAccessibilityStateChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 更新服务状态
        updateServiceStatus();
    }

    @Override
    protected void onDestroy() {
        //移除监听服务
        accessibilityManager.removeAccessibilityStateChangeListener(this);
        super.onDestroy();
    }

    @Override
    public void onAccessibilityStateChanged(boolean enabled) {
        // 更新服务状态
        updateServiceStatus();
    }

    public void openAccessibility(View view) {
        try {
            Intent accessibleIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(accessibleIntent);
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.turn_on_error_toast), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    /**
     * 更新当前 WorkService 显示状态
     */
    private void updateServiceStatus() {
        state.setChecked(isServiceEnabled());
    }

    /**
     * 获取 WorkService 是否启用状态
     *
     * @return
     */
    private boolean isServiceEnabled() {
        List<AccessibilityServiceInfo> accessibilityServices =
                accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        for (AccessibilityServiceInfo info : accessibilityServices) {
            if (info.getId().equals(getPackageName() + "/.WorkService")) {
                return true;
            }
        }
        return false;
    }
}
