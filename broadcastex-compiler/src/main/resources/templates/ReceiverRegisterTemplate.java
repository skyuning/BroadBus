package ${packageName};

import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import me.skyun.test.MainActivity;

public class ${className} {

    private void registerReceiver(Context context, BroadcastReceiver receiver, String action) {
        IntentFilter filter = new IntentFilter(action);
//        for (String c : categories) {
//            filter.addCategory(c);
//        }
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, filter);
    }

    public void process(Context context, final ${targetType} target) {
