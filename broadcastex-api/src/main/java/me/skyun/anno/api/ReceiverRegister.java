package me.skyun.anno.api;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by linyun on 16/11/8.
 */
public abstract class ReceiverRegister<T> {

    public static final String GEN_FILE_POSTFIX = "$$ReceiverRegister";

    protected void registerReceiver(Context context, BroadcastReceiver receiver, String action, String[] categories) {
        IntentFilter filter = new IntentFilter(action);
        for (String c : categories) {
            filter.addCategory(c);
        }
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, filter);
    }

    public static void register(Context context, Object target) {
        String registerClzName = target.getClass().getName() + GEN_FILE_POSTFIX;
        try {
            Class<? extends ReceiverRegister> registerClz = (Class<? extends ReceiverRegister>) Class.forName(registerClzName);
            ReceiverRegister register = registerClz.newInstance();
            register.registerReceiversForTarget(context, target);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    protected abstract void registerReceiversForTarget(Context context, T target);
}

