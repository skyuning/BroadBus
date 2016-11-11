package ${packageName};

import android.content.Context;
import android.content.BroadcastReceiver;
import me.skyun.broadcastex.api.ReceiverRegistrar;
import java.util.List;

public class ${className} extends ReceiverRegistrar<${targetType}> {

    @Override
    protected void registerReceivers(Context context, ${targetType} target, List<BroadcastReceiver> result) {
