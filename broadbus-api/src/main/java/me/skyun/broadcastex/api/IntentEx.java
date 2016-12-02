package me.skyun.broadcastex.api;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;

import junit.framework.Assert;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by linyun on 16/2/27.
 */
@SuppressLint("ParcelCreator")
public class IntentEx extends Intent {

    public IntentEx() {
        super();
    }

    public IntentEx(String action) {
        super(action);
    }

    public IntentEx(Class actionType) {
        super(getKey(actionType));
    }

    public IntentEx(Context packageContext, Class<?> cls) {
        super(packageContext, cls);
    }

    public IntentEx setAction(String action) {
        return (IntentEx) super.setAction(action);
    }

    public IntentEx setAction(Class actionType) {
        setAction(getKey(actionType));
        return this;
    }

    public IntentEx setFlags(int flags) {
        super.setFlags(flags);
        return this;
    }

    public IntentEx addCategories(Iterable categories) {
        for (Object category : categories) {
            addCategory(getKey(category));
        }
        return this;
    }

    public IntentEx addCategories(Object... categories) {
        for (Object category : categories) {
            addCategory(getKey(category));
        }
        return this;
    }

    public IntentEx putKeyValueExtras(Object... keyValues) {
        for (int i = 0; i < keyValues.length; i += 2) {
            putExtraEx((String) keyValues[i], keyValues[i + 1]);
        }
        return this;
    }

    /**
     * 简便的putExtra函数，以object的Class全名作为key，object做为value
     * 优点：不用写一堆key常量
     * 缺点：同一类型的object只能有一个
     */
    public IntentEx putSimpleExtras(Object... objects) {
        for (Object obj : objects) {
            if (obj == null) {
                continue;
            }

            // Uri是一个abstract基类，实际用的类型一般是其内部定议的StringUri等，所以这里单独处理一下
            if (obj instanceof Uri) {
                putExtra(Uri.class, obj);
                continue;
            }

            // 必需要是public的类
            Assert.assertTrue("The receiver has no way to receive Data of none public class",
                    Modifier.isPublic(obj.getClass().getModifiers()));

            if (obj instanceof List) {
                if (((List) obj).isEmpty()) {
                    continue;
                }
                Class elementClz = ((List) obj).get(0).getClass();
                putExtraEx(getListExtraKey(elementClz), obj);
            } else {
                putExtra(obj.getClass(), obj);
            }
        }
        return this;
    }

    public IntentEx putExtra(Class keyClz, Object value) {
        putExtraEx(getKey(keyClz), value);
        return this;
    }

    private IntentEx putExtraEx(String key, Object value) {
        if (getExtras() != null && getExtras().get(key) != null) {
            throw new IllegalArgumentException("Duplicate key: " + key);
        }

        if (value instanceof Serializable) {
            return (IntentEx) putExtra(key, (Serializable) value);
        } else if (value instanceof Parcelable) {
            return (IntentEx) putExtra(key, (Parcelable) value);
        }

        try {
            Method putExtraMethod = getClass().getMethod("putExtra", String.class, value.getClass());
            putExtraMethod.setAccessible(true);
            putExtraMethod.invoke(this, key, value);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return this;
    }

    public IntentEx broadcast(Context context, Object... objects) {
        putSimpleExtras(objects);
        if (context instanceof Activity) {
            activityBroadcast((Activity) context);
        } else {
            localBroadcast(context);
        }
        return this;
    }

    public void localBroadcast(Context context) {
        if (context != null) {
            LocalBroadcastManager.getInstance(context).sendBroadcastSync(this);
        }
    }

    public IntentEx activityBroadcast(Activity activity) {
        if (activity != null) {
            addCategory(activity.toString());
            LocalBroadcastManager.getInstance(activity).sendBroadcastSync(this);
        }
        return this;
    }

    public void startActivity(Context context) {
        context.startActivity(this);
    }

    public void startActivity(Activity activity) {
        activity.startActivity(this);
    }

    public void startActivity(Fragment fragment) {
        fragment.startActivity(this);
    }

    public void startActivityForResult(Fragment fragment, int requestCode) {
        fragment.startActivityForResult(this, requestCode);
    }

    public void startActivityForResult(Activity activity, int requestCode) {
        activity.startActivityForResult(this, requestCode);
    }

    public void setResult(Activity activity, int resultCode) {
        activity.setResult(resultCode, this);
    }

    public static <T> T getExtra(Intent intent, Class<T> clz) {
        return (T) intent.getExtras().get(getKey(clz));
    }

    public static <E> List<E> getListExtra(Intent intent, Class<E> clz) {
        List<E> list = null;

        // 可能Intent中不带有: Bundle, 例如: startActivtyForResult 返回时直接放弃
        if (intent.getExtras() != null) {
            list = (List<E>) intent.getExtras().get(getListExtraKey(clz));
        }
        if (list == null) {
            list = new ArrayList<>(0);
        }
        return list;
    }

    private static String getListExtraKey(Class itemClz) {
        return getKey(List.class) + "<" + getKey(itemClz) + ">";
    }

    public static String getKey(Object obj) {
        return obj.toString();
    }

    public static String getKey(Class clz) {
        return clz.getName().replace("$", "."); // 用getName还是getCanonicalName?
    }
}
