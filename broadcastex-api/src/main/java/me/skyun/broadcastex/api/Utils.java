package me.skyun.broadcastex.api;

import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by linyun on 16/11/12.
 */
class Utils {
    public static void logException(Exception e) {
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        e.printStackTrace(printWriter);
        printWriter.flush();
        printWriter.close();
        Log.d(e.getStackTrace()[0].getMethodName(), writer.toString());
    }

    public static String filterToString(IntentFilter filter) {
        StringBuilder sb = new StringBuilder("[action: ");
        for (int i=0; i<filter.countActions(); i++) {
            sb.append(filter.getAction(i)).append(", ");
        }
        sb.append("]");
        sb.append(" categories[");
        for (int i=0; i<filter.countCategories(); i++) {
            sb.append(filter.getCategory(i)).append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    public static String paramTypesToString(String[] paramTypes) {
        return "(" + TextUtils.join(", ", paramTypes) + ")";
    }
}
