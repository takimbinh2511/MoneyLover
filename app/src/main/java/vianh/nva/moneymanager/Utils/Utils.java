package vianh.nva.moneymanager.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.util.Locale;

public class Utils {
    public static int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static String currencyFormat(long n) {
        Locale vn = new Locale("vi", "VN");
        return NumberFormat.getNumberInstance(vn).format(n);
    }

}
