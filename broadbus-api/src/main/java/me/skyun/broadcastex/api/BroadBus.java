package me.skyun.broadcastex.api;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
 * <p>
 * Activity用{@link android.app.Application.ActivityLifecycleCallbacks}来全局处理
 * 在{@link Activity#onCreate(Bundle)}时register，在{@link Activity#onDestroy()}时unregister
 * 根据具体需要，可自己调用{@link #unregisterTarget(Object)}提前unregister
 * <p>
 * Fragment无法全局处理，需要自己埋点到Fragment的生命周期中
 */
public class BroadBus {

    static final String ACTION_FRAGMENT_VIEW_CREATED =
            BroadBus.class.getName() + ".ACTION_FRAGMENT_VIEW_CREATED";

    /**
     * statics
     */
    private static BroadBus mInstance;

    public static BroadBus getInstance() {
        if (mInstance == null) {
            mInstance = new BroadBus();
        }
        return mInstance;
    }

    public static void setUp(Application application) {
        Log.d(BroadBus.class.getName(), "setUp");
        getInstance().mContext = application.getApplicationContext();
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                getInstance().registerTarget(activity);
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
                getInstance().unregisterTarget(activity);
            }
        });
    }

    /**
     * members
     */
    // 保存target和receiver的对应关系，在target destroy时unregister对应的receivers
    private Map<String, List<BroadcastReceiver>> mReceiversByTarget = new HashMap<>();
    private Context mContext;

    public <T> void registerTarget(T target) {
        Log.d(BroadBus.class.getName(), "registerTarget");
        List<BroadcastReceiver> receivers = new ArrayList<>();
        Class curTargetClz = target.getClass();
        do {
            try {
                String registrarClzName = curTargetClz.getName() + ReceiverRegistrar.REGISTER_POSTFIX;

                Log.d(BroadBus.class.getName(), "registerTarget: load " + registrarClzName);
                Class<ReceiverRegistrar<T>> registrarClz =
                        (Class<ReceiverRegistrar<T>>) Class.forName(registrarClzName);
                Log.d(BroadBus.class.getName(), "registerTarget: " + registrarClzName + " loaded");

                Constructor<ReceiverRegistrar<T>> constructor = registrarClz.getConstructor();
                ReceiverRegistrar<T> registrar = constructor.newInstance();

                receivers.addAll(registrar.registerReceivers(mContext, target));
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
            curTargetClz = curTargetClz.getSuperclass();
        } while (curTargetClz != null);
        mReceiversByTarget.put(getTargetKey(target), receivers);
    }

    public void unregisterTarget(Object target) {
        String key = getTargetKey(target);
        if (mReceiversByTarget.containsKey(key)) {
            for (BroadcastReceiver receiver : mReceiversByTarget.remove(key)) {
                LocalBroadcastManager.getInstance(mContext).unregisterReceiver(receiver);
            }
        }
    }

    /**
     * @param activity
     * @param dataList 既是要post的action，又是要post的数据
     * @return
     */
    public static Intent post(Activity activity, Object... dataList) {
        return postAction(activity, dataList[0].getClass(), dataList);
    }

    public static Intent postAction(Activity activity, Class actionType, Object... dataList) {
        return postAction(activity, IntentEx.getKey(actionType), dataList);
    }

    public static Intent postAction(Activity activity, String action, Object... dataList) {
        return new IntentEx(action).putSimpleExtras(dataList).activityBroadcast(activity);
    }

    public static Intent post(Fragment fragment, Object object) {
        if (fragment.getActivity() != null) {
            return new IntentEx().setAction(object.getClass())
                    .putSimpleExtras(object)
                    .activityBroadcast(fragment.getActivity());
        }
        return null;
    }

    public static Intent postFragmentViewCreated() {
        return new IntentEx().setAction(ACTION_FRAGMENT_VIEW_CREATED).broadcast(getInstance().mContext);
    }

    private static String getTargetKey(Object target) {
        return target.toString();
    }
}

