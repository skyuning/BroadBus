package me.skyun.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import me.skyun.anno.api.BroadcastExReceiver;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @BroadcastExReceiver
    public void test(Context context, Intent intent) {
    }
}
