package ${packageName};

import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import me.skyun.test.MainActivity;
import me.skyun.anno.api.ReceiverRegister;

public class ${className} extends ReceiverRegister<${targetType}> {

    @Override
    protected void registerReceiversForTarget(Context context, final ${targetType} target) {
