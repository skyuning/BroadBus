package me.skyun.broadcastex.api;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by linyun on 16/11/8.
 */
public abstract class ReceiverRegistrar<T> {

    public static final String REGISTER_POSTFIX = "$$ReceiverRegistrar";

    protected abstract List<BroadcastReceiver> registerReceivers(Context context, T target);

    protected Object[] getParamValues(Context context, Intent intent, String[] paramTypes) {
        Object[] paramValues = new Object[paramTypes.length];
        if (intent.getExtras() != null) {
            for (int i = 0; i < paramValues.length; i++) {
                if (paramTypes[i].equals(Context.class.getCanonicalName())) {
                    paramValues[i] = context;
                } else if (paramTypes[i].equals(Intent.class.getCanonicalName())) {
                    paramValues[i] = intent;
                } else {
                    paramValues[i] = intent.getExtras().get(paramTypes[i]);
                }
            }
        }
        return paramValues;
    }

    protected static <T> BroadcastReceiver registerReceiver(Context context, T target, boolean isFragmentRefresher,
            BroadcastReceiver receiver, String[] actions, String[] categories) {

        IntentFilter filter = new IntentFilter();
        if (isFragmentRefresher) {
            filter.addAction(BroadBus.ACTION_FRAGMENT_VIEW_CREATED);
        }
        for (String a : actions) {
            filter.addAction(a);
        }
        for (String c : categories) {
            filter.addCategory(c);
        }
        filter.addCategory(getTargetCategory(target));

        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, filter);
        Log.d(ReceiverRegistrar.class.getName() + ".registerReceiver", Utils.filterToString(filter));
        return receiver;
    }

    private static void invokeMethod(Object target, String methodName, String[] paramTypeNames, Object[] paramValues)
            throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class[] paramTypes = new Class[paramTypeNames.length];
        for (int i = 0; i < paramTypes.length; i++) {
            paramTypes[i] = Class.forName(paramTypeNames[i]);
        }
        Log.d(ReceiverRegistrar.class.getName(), "invokeMethod: "
                + target.getClass().getName() + "." + methodName + Utils.paramTypesToString(paramTypeNames));
        Method method = target.getClass().getMethod(methodName, paramTypes);
        method.invoke(target, paramValues);
    }

    private static String getTargetCategory(Object target) {
        if (target instanceof Activity) {
            return target.toString();
        } else if (target instanceof Fragment) {
            return ((Fragment) target).getActivity().toString();
        }
        throw new RuntimeException("暂时只支持Activity和Fragment");
    }
}

