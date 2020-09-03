package com.asdoi.quicksettings;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.ArrayMap;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.asdoi.quicksettings.tiles.PlayPauseTileService;

import java.util.Map;

public class SettingsActivity extends AppCompatActivity {
    public static String PREF_PLAY_PAUSE = "play_pause";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        TextView credits = findViewById(R.id.credits);
        credits.setText(Html.fromHtml(getString(R.string.credits), Html.FROM_HTML_MODE_LEGACY));
        credits.setMovementMethod(LinkMovementMethod.getInstance());

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
    }

    public static boolean isTileEnabled(Context context, Class<?> service) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(getPreferenceToService(service), false);
    }

    public static ArrayMap<String, Class<?>> getPreferenceService() {
        ArrayMap<String, Class<?>> servicePreferences = new ArrayMap<>();
        servicePreferences.put(PREF_PLAY_PAUSE, PlayPauseTileService.class);
        return servicePreferences;
    }

    public static ArrayMap<Class<?>, String> getServicePreference() {
        ArrayMap<Class<?>, String> newMap = new ArrayMap<>();
        for (Map.Entry<String, Class<?>> entry : getPreferenceService().entrySet())
            newMap.put(entry.getValue(), entry.getKey());
        return newMap;
    }


    public static String getPreferenceToService(Class<?> service) {
        return getServicePreference().get(service);
    }
}