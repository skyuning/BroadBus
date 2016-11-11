package me.skyun.broadcastex.api;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by linyun on 16/11/8.
 */
public abstract class ReceiverRegistrar<T> {

    public static final String REGISTER_POSTFIX = "$$ReceiverRegistrar";

    protected abstract void registerReceivers(Context context, T target, List<BroadcastReceiver> result);

    protected static BroadcastReceiver registerReceiver(final Context context, String action, String[] categories,
            final Object target, final String methodName, final String[] paramTypeNames) {

        IntentFilter filter = new IntentFilter(action);
        for (String c : categories) {
            filter.addCategory(c);
        }
        filter.addCategory(getTargetCategory(target));

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Object[] paramValues = new Object[paramTypeNames.length];
                if (intent.getExtras() != null) {
                    for (int i = 0; i < paramValues.length; i++) {
                        if (paramTypeNames[i].equals(Context.class.getCanonicalName())) {
                            paramValues[i] = context;
                        } else if (paramTypeNames[i].equals(Intent.class.getCanonicalName())) {
                            paramValues[i] = intent;
                        } else {
                            paramValues[i] = intent.getExtras().get(paramTypeNames[i]);
                        }
                    }
                }
                try {
                    invokeMethod(target, methodName, paramTypeNames, paramValues);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        };

        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, filter);
        return receiver;
    }

    private static void invokeMethod(Object target, String methodName, String[] paramTypeNames, Object[] paramValues)
            throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class[] paramTypes = new Class[paramTypeNames.length];
        for (int i = 0; i < paramTypes.length; i++) {
            paramTypes[i] = Class.forName(paramTypeNames[i]);
        }
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

