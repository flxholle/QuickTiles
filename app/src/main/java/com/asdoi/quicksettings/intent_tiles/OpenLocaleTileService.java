package com.asdoi.quicksettings.intent_tiles;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Icon;
import android.os.LocaleList;
import android.widget.Toast;

import com.asdoi.quicksettings.R;
import com.asdoi.quicksettings.abstract_tiles.ModifySystemSettingsTileService;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class OpenLocaleTileService extends ModifySystemSettingsTileService<Locale> {

    @Override
    public void onClick() {
        unlockAndRun(super::onClick);
    }

    @Override
    public void reset() {

    }

    @Override
    public boolean isActive(@NotNull Locale value) {
        return false;
    }

    @NotNull
    @Override
    public List<Locale> getValueList() {
        try {
            Class localePickerClass = Class.forName("com.android.internal.app.LocalePicker");
            Method getLocalesMethod = localePickerClass.getMethod("getLocales");
            LocaleList userLocales = (LocaleList) getLocalesMethod.invoke(localePickerClass);
            List<Locale> localeList = new ArrayList<>();
            for (int i = 0; i < userLocales.size(); i++) {
                localeList.add(userLocales.get(i));
            }
            return localeList;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), "Error getting user languages", Toast.LENGTH_LONG).show();
        }
        return Collections.singletonList(queryValue());
    }

    @NotNull
    @Override
    public Locale queryValue() {
        Context context = getBaseContext();
        Resources res = context.getResources();
        return res.getConfiguration().getLocales();
    }

    @SuppressLint("PrivateApi")
    @Override
    public boolean saveValue(@NotNull Locale value) {
        try {

//            Method method1 = localePicker.getMethod("getSupportedLocales", Context.class);
//            String[] locales = (String[]) method1.invoke(localePicker,this);


            Class localePicker2 = Class.forName("com.android.internal.app.LocalePicker");
//            Method updateLocale = localePicker2.getMethod("updateLocale", Locale.class);
//            Method getLocales = localePicker2.getMethod("getLocale", Locale.class);
//            LocaleList locales2 = (LocaleList) getLocales.invoke(null, null);
//
//            Configuration config = getResources().getConfiguration();
//            config.setLocale(Locale.CANADA);
//            getResources().updateConfiguration(config, getResources().getDisplayMetrics());

//            Class.forName("com.android.internal.app.LocalePicker")
//                    .getMethod("updateLocale", Locale.class)
//                    .invoke(null, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateIcon() {
        return false;
    }

    @NotNull
    @Override
    public Icon getIcon(@NotNull Locale value) {
        return Icon.createWithResource(getApplicationContext(), R.drawable.ic_language);
    }

    @NotNull
    @Override
    public CharSequence getLabel(@NotNull Locale value) {
        return null;
    }
}
