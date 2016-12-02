package me.skyun.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import me.skyun.broadcastex.api.BroadBus;
import me.skyun.broadcastex.api.BroadBusReceiver;

public class MainActivity extends FragmentActivity {

//    @ViewBinder(id = R.id.root)
//    protected View mRootView = findViewById(R.id.root);

    private Detail mDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mRootView.setVisibility(View.VISIBLE);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, MainActivity.class));

//                mDetail = new Detail("test post 2");
                mDetail.s = "test post 2";
                Intent intent = BroadBus.post(MainActivity.this, mDetail);
            }
        });

        TestFragment fragment = new TestFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.root, fragment).commit();
        getSupportFragmentManager().executePendingTransactions();
        mDetail = new Detail("test post");
        Intent intent = BroadBus.post(MainActivity.this, mDetail);
        Log.d("skyun-debug", "Send Broadcast: " + intent.toString());
    }

    @BroadBusReceiver(actionTypes = Actions.TestAction.class,
            categoryTypes = {Integer.class, String.class},
            categories = {"aa", "bb", "cc"})
    public void test(String sss) {
//        Toast.makeText(this, "broadcast received in: " + this.toString(), Toast.LENGTH_SHORT).show();
    }

    @BroadBusReceiver(actionTypes = String.class,
            categoryTypes = {Integer.class, String.class},
            categories = {"aa"})
    public void test2(Context context, String sss, Float fff) {
//        Toast.makeText(this, "broadcast received in: " + this.toString(), Toast.LENGTH_SHORT).show();
    }
}
