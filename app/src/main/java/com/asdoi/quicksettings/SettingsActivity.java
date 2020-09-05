package com.asdoi.quicksettings;

import android.os.Bundle;
import android.util.ArrayMap;

import androidx.appcompat.app.AppCompatActivity;

import com.asdoi.quicksettings.tiles.AdaptiveBrightnessTileService;
import com.asdoi.quicksettings.tiles.AnimatorDurationTileService;
import com.asdoi.quicksettings.tiles.BatteryTileService;
import com.asdoi.quicksettings.tiles.BrightnessTileService;
import com.asdoi.quicksettings.tiles.CounterTileService;
import com.asdoi.quicksettings.tiles.DemoModeTileService;
import com.asdoi.quicksettings.tiles.FinishActivitiesTileService;
import com.asdoi.quicksettings.tiles.GrayscaleTileService;
import com.asdoi.quicksettings.tiles.KeepScreenOnTileService;
import com.asdoi.quicksettings.tiles.MakeCallTileService;
import com.asdoi.quicksettings.tiles.MediaVolumeTileService;
import com.asdoi.quicksettings.tiles.NewAlarmTileService;
import com.asdoi.quicksettings.tiles.NewCalendarEventTileService;
import com.asdoi.quicksettings.tiles.NewTimerTileService;
import com.asdoi.quicksettings.tiles.NextSongTileService;
import com.asdoi.quicksettings.tiles.OpenAboutPhoneTileService;
import com.asdoi.quicksettings.tiles.OpenCalculatorTileService;
import com.asdoi.quicksettings.tiles.OpenCameraTileService;
import com.asdoi.quicksettings.tiles.OpenConnectedDevicesTileService;
import com.asdoi.quicksettings.tiles.OpenDataUsageTileService;
import com.asdoi.quicksettings.tiles.OpenFilesTileService;
import com.asdoi.quicksettings.tiles.OpenNotificationLogTileService;
import com.asdoi.quicksettings.tiles.OpenSettingsSearchTileService;
import com.asdoi.quicksettings.tiles.OpenVolumePanelTileService;
import com.asdoi.quicksettings.tiles.OpenVpnTileService;
import com.asdoi.quicksettings.tiles.PlayPauseTileService;
import com.asdoi.quicksettings.tiles.PreviousSongTileService;
import com.asdoi.quicksettings.tiles.ShowTapsTileService;
import com.asdoi.quicksettings.tiles.ToggleAnimationTileService;
import com.asdoi.quicksettings.tiles.UsbDebuggingTileService;
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
        servicePreferences.put("grayscale", GrayscaleTileService.class);
        servicePreferences.put("adaptive_brightness", AdaptiveBrightnessTileService.class);
        servicePreferences.put("usb_debugging", UsbDebuggingTileService.class);
        servicePreferences.put("finish_activities", FinishActivitiesTileService.class);
        servicePreferences.put("keep_screen_on", KeepScreenOnTileService.class);
        servicePreferences.put("demo_mode", DemoModeTileService.class);
        servicePreferences.put("animator_duration", AnimatorDurationTileService.class);
        servicePreferences.put("show_taps", ShowTapsTileService.class);
        servicePreferences.put("disable_all_animations", ToggleAnimationTileService.class);
        servicePreferences.put("new_alarm", NewAlarmTileService.class);
        servicePreferences.put("new_timer", NewTimerTileService.class);
        servicePreferences.put("open_camera", OpenCameraTileService.class);
        servicePreferences.put("open_calculator", OpenCalculatorTileService.class);
        servicePreferences.put("open_volume_panel", OpenVolumePanelTileService.class);
        servicePreferences.put("adjust_brightness", BrightnessTileService.class);
        servicePreferences.put("open_files", OpenFilesTileService.class);
        servicePreferences.put("battery", BatteryTileService.class);
        servicePreferences.put("vpn", OpenVpnTileService.class);
        servicePreferences.put("data_usage", OpenDataUsageTileService.class);
        servicePreferences.put("about_phone", OpenAboutPhoneTileService.class);
        servicePreferences.put("connected_devices", OpenConnectedDevicesTileService.class);
        servicePreferences.put("notification_history", OpenNotificationLogTileService.class);
        servicePreferences.put("search_settings", OpenSettingsSearchTileService.class);
        servicePreferences.put("new_event", NewCalendarEventTileService.class);
        servicePreferences.put("make_call", MakeCallTileService.class);
        servicePreferences.put("counter", CounterTileService.class);
        return servicePreferences;
    }

    public static ArrayList<Class<?>> getSecureSettingsServices() {
        ArrayList<Class<?>> services = new ArrayList<>();
        services.add(GrayscaleTileService.class);
        services.add(UsbDebuggingTileService.class);
        services.add(FinishActivitiesTileService.class);
        services.add(KeepScreenOnTileService.class);
        services.add(AnimatorDurationTileService.class);
        services.add(ShowTapsTileService.class);
        services.add(ToggleAnimationTileService.class);
        return services;
    }

    public static ArrayList<Class<?>> getModifySystemSettingsServices() {
        ArrayList<Class<?>> services = new ArrayList<>();
        services.add(AdaptiveBrightnessTileService.class);
        services.add(BrightnessTileService.class);
        return services;
    }

    public static ArrayList<Class<?>> getSecureSettingsAndDumpServices() {
        ArrayList<Class<?>> services = new ArrayList<>();
        services.add(DemoModeTileService.class);
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