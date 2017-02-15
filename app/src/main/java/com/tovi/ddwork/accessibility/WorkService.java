package com.tovi.ddwork.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

/**
 * @author <a href='mailto:zhaotengfei9@gmail.com'>Tengfei Zhao</a>
 */
// TODO 待开发
public class WorkService extends AccessibilityService {
    private static final String TO_OFF_WORK = "下班打卡";
    private static final String TO_ON_WORK = "ShangBan";
    private static final String UPDATE_RECORD = "GengXin";
    private static final String TAG = "WorkService";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        work(event);
    }

    @Override
    public void onInterrupt() {

    }

    private void work(AccessibilityEvent event) {
        Log.d(TAG, "work: ==================");
    }
}
