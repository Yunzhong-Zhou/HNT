package com.app.hnt.utils.changelanguage;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Locale;


/**
 * 切换语言
 */
public class LanguageUtil {

    private static final String TAG = "LanguageUtil";

    /**
     * @param context
     * @param newLanguage 想要切换的语言类型 比如 "en" ,"zh"
     */
    @SuppressWarnings("deprecation")
    public static void changeAppLanguage(Context context, String newLanguage) {
        if (TextUtils.isEmpty(newLanguage)) {
            return;
        }
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        //获取想要切换的语言类型
        Locale locale = getLocaleByLanguage(newLanguage);
        configuration.setLocale(locale);
        // updateConfiguration
        resources.updateConfiguration(configuration, dm);

//        resources.flushLayoutCache();
    }

    public static Locale getLocaleByLanguage(String language) {
        Locale locale = Locale.US;
        if (language.equals(LanguageType.CHINESE.getLanguage())) {//中文
            locale = Locale.SIMPLIFIED_CHINESE;
        } else if (language.equals(LanguageType.ENGLISH.getLanguage())) {//英文
            locale = Locale.US;
//            locale = new Locale("en", "US");
        } else if (language.equals(LanguageType.JAPANESE.getLanguage())) {//日文
            locale = Locale.JAPAN;
        } else if (language.equals(LanguageType.KOREAN.getLanguage())) {//韩文
            locale = Locale.KOREA;
        } else if (language.equals(LanguageType.VIETNAMESE.getLanguage())) {//越南文
            locale = new Locale("vi", "VN");
        } else if (language.equals(LanguageType.THAILAND.getLanguage())) {//泰文
            locale = Locale.forLanguageTag(language);
        }
        Log.d(TAG, "getLocaleByLanguage: " + locale.getDisplayName());
        return locale;
    }

    public static Context attachBaseContext(Context context, String language) {
        Log.d(TAG, "attachBaseContext: " + Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, language);
        } else {
            return context;
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, String language) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Locale locale = LanguageUtil.getLocaleByLanguage(language);

        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
//        configuration.setLocales(new LocaleList(locale));
        resources.updateConfiguration(configuration, dm);

        return context.createConfigurationContext(configuration);
    }
}
