package ${packageName};

import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

public class ${className} {

    private void registerReceiver(Context context, String action, String[] categories, BroadcastReceiver receiver) {
        IntentFilter filter = new IntentFilter(action);
        for (String c : categories) {
            filter.addCategory(c);
        }
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, filter);
    }

    public void process(Context context) {
