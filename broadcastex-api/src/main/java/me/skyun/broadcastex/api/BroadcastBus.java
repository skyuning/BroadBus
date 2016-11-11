package me.skyun.broadcastex.api;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by linyun on 16/11/11.
 * <p/>
 * Activity用{@link android.app.Application.ActivityLifecycleCallbacks}来全局处理
 * 在{@link Activity#onCreate(Bundle)}时register，在{@link Activity#onDestroy()}时unregister
 * 根据具体需要，可自己调用{@link #unregisterTarget(Context, Object)}提前unregister
 * <p/>
 * Fragment无法全局处理，需要自己埋点到Fragment的生命周期中
 */
public class BroadcastBus {

    /**
     * statics
     */
    private static BroadcastBus mInstance;

    public static BroadcastBus getInstance() {
        if (mInstance == null) {
            mInstance = new BroadcastBus();
        }
        return mInstance;
    }

    public static void setUp(Application application) {
        Log.d(BroadcastBus.class.getName(), "setUp");
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                getInstance().registerTarget(activity.getApplicationContext(), activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                getInstance().unregisterTarget(activity.getApplicationContext(), activity);
            }
        });
    }

    /**
     * members
     */
    // 保存target和receiver的对应关系，在target destroy时unregister对应的receivers
    private Map<String, List<BroadcastReceiver>> mReceiversByTarget = new HashMap<>();

    public <T> void registerTarget(Context context, T target) {
        Log.d(BroadcastBus.class.getName(), "registerTarget");
        try {
            String registrarClzName = target.getClass().getName() + ReceiverRegistrar.REGISTER_POSTFIX;
            Log.d(BroadcastBus.class.getName(), "registerTarget: load " + registrarClzName);
            Class<ReceiverRegistrar<T>> registrarClz = (Class<ReceiverRegistrar<T>>) Class.forName(registrarClzName);
            Log.d(BroadcastBus.class.getName(), "registerTarget: " + registrarClzName + " loaded");
            Constructor<ReceiverRegistrar<T>> constructor = registrarClz.getConstructor();
            ReceiverRegistrar<T> registrar = constructor.newInstance();

            List<BroadcastReceiver> result = new ArrayList<>();
            mReceiversByTarget.put(getTargetKey(target), result);
            registrar.registerReceivers(context, target, result);
        } catch (ClassNotFoundException ignored) {
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void unregisterTarget(Context context, Object target) {
        String key = getTargetKey(target);
        if (mReceiversByTarget.containsKey(target)) {
            for (BroadcastReceiver receiver : mReceiversByTarget.remove(key)) {
                LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
            }
        }
    }

    private static String getTargetKey(Object target) {
        return target.toString();
    }
}

