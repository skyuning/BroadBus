package me.skyun.test;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.TextView;

import me.skyun.broadcastex.api.BroadBus;

/**
 * Created by linyun on 16/11/2.
 */
public class TestApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        BroadBus.setUp(this);
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Log.d("debug", "onActivityCreate");
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Log.d("debug", "onActivityStarted");
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Log.d("debug", "onActivityResumed");
                View decorView = activity.getWindow().getDecorView();
                traverseViewTree(decorView);
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Log.d("debug", "onActivityPaused");
            }

            @Override
            public void onActivityStopped(Activity activity) {
                Log.d("debug", "onActivityStoped");
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                Log.d("debug", "onActivitySaveInstanceState");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.d("debug", "onActivityDestroyed");
            }
        });
    }

    private void traverseViewTree(View root) {
        root.setAccessibilityDelegate(mDelegate);
        if (root instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) root).getChildCount(); i++) {
                traverseViewTree(((ViewGroup) root).getChildAt(i));
            }
        }
    }

    private TestDelegate mDelegate = new TestDelegate();

    private static class TestDelegate extends View.AccessibilityDelegate {
        @Override
        public void sendAccessibilityEvent(View host, int eventType) {
            super.sendAccessibilityEvent(host, eventType);
            String msg = "";
            if (host instanceof TextView) {
                msg = ((TextView) host).getText().toString();
            }
            if (eventType == AccessibilityEvent.TYPE_VIEW_CLICKED) {
                Log.d("debug", "Click Event: " + msg);
            }
        }
    }
}
