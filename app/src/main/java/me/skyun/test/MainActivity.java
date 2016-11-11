package me.skyun.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import me.skyun.broadcastex.api.BroadcastExReceiver;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent0 = new Intent(Actions.class.getName().replace("$", "."));
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent0);

        Intent intent = new Intent(Actions.TestAction.class.getName().replace("$", "."));
        intent.addCategory(this.toString());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        Log.d("skyun-debug", "Send Broadcast: " + intent.toString());

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
            }
        });
    }

    @BroadcastExReceiver(actionType = Actions.TestAction.class,
            categoryTypes = {Integer.class, String.class},
            categories = {"aa", "bb", "cc"})
    public void test(Context context, Intent intent, String sss, Integer iii) {
        Toast.makeText(this, "broadcast received in: " + this.toString(), Toast.LENGTH_SHORT).show();
    }

    @BroadcastExReceiver(actionType = Actions.class,
            categoryTypes = {Integer.class, String.class},
            categories = {"aa"})
    public void test2(Context context, String sss, Float fff) {
        Toast.makeText(this, "broadcast received in: " + this.toString(), Toast.LENGTH_SHORT).show();
    }
}
