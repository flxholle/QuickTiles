package com.asdoi.quicksettings;

import android.os.Bundle;
import android.util.ArrayMap;

import androidx.appcompat.app.AppCompatActivity;

import com.asdoi.quicksettings.tiles.AdaptiveBrightnessService;
import com.asdoi.quicksettings.tiles.AnimatorDurationService;
import com.asdoi.quicksettings.tiles.DemoModeService;
import com.asdoi.quicksettings.tiles.FinishActivitiesService;
import com.asdoi.quicksettings.tiles.GrayscaleService;
import com.asdoi.quicksettings.tiles.KeepScreenOnService;
import com.asdoi.quicksettings.tiles.MediaVolumeTileService;
import com.asdoi.quicksettings.tiles.NewAlarmTileService;
import com.asdoi.quicksettings.tiles.NextSongTileService;
import com.asdoi.quicksettings.tiles.PlayPauseTileService;
import com.asdoi.quicksettings.tiles.PreviousSongTileService;
import com.asdoi.quicksettings.tiles.ShowTapsService;
import com.asdoi.quicksettings.tiles.ToggleAnimationService;
import com.asdoi.quicksettings.tiles.UsbDebuggingService;
import com.bytehamster.lib.preferencesearch.SearchPreferenceResult;
import com.bytehamster.lib.preferencesearch.SearchPreferenceResultListener;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity implements SearchPreferenceResultListener {
    private SettingsFragment prefsFragment;

    public static ArrayMap<String, Class<?>> getPreferenceService() {
        ArrayMap<String, Class<?>> servicePreferences = new ArrayMap<>();
        servicePreferences.put("play_pause", PlayPauseTileService.class);
        servicePreferences.put("next_song", NextSongTileService.class);
        servicePreferences.put("previous_song", PreviousSongTileService.class);
        servicePreferences.put("media_volume", MediaVolumeTileService.class);
        servicePreferences.put("grayscale", GrayscaleService.class);
        servicePreferences.put("adaptive_brightness", AdaptiveBrightnessService.class);
        servicePreferences.put("usb_debugging", UsbDebuggingService.class);
        servicePreferences.put("finish_activities", FinishActivitiesService.class);
        servicePreferences.put("keep_screen_on", KeepScreenOnService.class);
        servicePreferences.put("demo_mode", DemoModeService.class);
        servicePreferences.put("animator_duration", AnimatorDurationService.class);
        servicePreferences.put("show_taps", ShowTapsService.class);
        servicePreferences.put("disable_all_animations", ToggleAnimationService.class);
        servicePreferences.put("new_alarm", NewAlarmTileService.class);
        return servicePreferences;
    }

    public static ArrayList<Class<?>> getSecureSettingsServices() {
        ArrayList<Class<?>> services = new ArrayList<>();
        services.add(GrayscaleService.class);
        services.add(UsbDebuggingService.class);
        services.add(FinishActivitiesService.class);
        services.add(KeepScreenOnService.class);
        services.add(AnimatorDurationService.class);
        services.add(ShowTapsService.class);
        services.add(ToggleAnimationService.class);
        return services;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        prefsFragment = new SettingsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, prefsFragment)
                .commit();
    }

    @Override
    public void onSearchResultClicked(SearchPreferenceResult result) {
        result.closeSearchPage(this);
        result.highlight(prefsFragment);
    }
}