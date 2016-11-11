package me.skyun.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Toast;

import me.skyun.broadcastex.api.BroadcastExReceiver;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(Actions.TestAction.class.getCanonicalName());
        intent.addCategory(this.toString());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

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

    @BroadcastExReceiver(actionType = Actions.TestAction.class,
            categoryTypes = {Integer.class, String.class},
            categories = {"aa", "bb"})
    public void test2(Context context, Intent intent, String sss, Integer iii) {
        Toast.makeText(this, "broadcast received in: " + this.toString(), Toast.LENGTH_SHORT).show();
    }
}
