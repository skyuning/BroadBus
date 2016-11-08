package me.skyun.test;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import me.skyun.anno.api.BroadcastExReceiver;

public class MainActivity extends FragmentActivity {

    @Override
    @BroadcastExReceiver
    protected void onStart() {
        super.onStart();
    }

    @Override
    @BroadcastExReceiver
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
