package me.skyun.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

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

        Intent intent = new Intent(Actions.TestAction.class.getCanonicalName());
        intent.addCategory(this.toString());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @BroadcastExReceiver(actionType = Actions.TestAction.class,
            categoryTypes = {Integer.class, String.class},
            categories = {"aa", "bb"})
    public void test(Context context, Intent intent) {
        Toast.makeText(MainActivity.this, "broadcast received", Toast.LENGTH_SHORT).show();
    }
}
