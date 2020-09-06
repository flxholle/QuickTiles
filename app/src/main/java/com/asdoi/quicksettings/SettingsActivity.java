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
import com.asdoi.quicksettings.tiles.DoNotDisturbSwitchTileService;
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
import com.asdoi.quicksettings.tiles.OpenAppOneTileService;
import com.asdoi.quicksettings.tiles.OpenCalculatorTileService;
import com.asdoi.quicksettings.tiles.OpenCastTileService;
import com.asdoi.quicksettings.tiles.OpenConnectedDevicesTileService;
import com.asdoi.quicksettings.tiles.OpenDataUsageTileService;
import com.asdoi.quicksettings.tiles.OpenDeveloperOptionsTileService;
import com.asdoi.quicksettings.tiles.OpenFilesTileService;
import com.asdoi.quicksettings.tiles.OpenLocaleTileService;
import com.asdoi.quicksettings.tiles.OpenNotificationLogTileService;
import com.asdoi.quicksettings.tiles.OpenSettingsSearchTileService;
import com.asdoi.quicksettings.tiles.OpenVolumePanelTileService;
import com.asdoi.quicksettings.tiles.OpenVpnTileService;
import com.asdoi.quicksettings.tiles.PlayPauseTileService;
import com.asdoi.quicksettings.tiles.PreviousSongTileService;
import com.asdoi.quicksettings.tiles.RecordVideoTileService;
import com.asdoi.quicksettings.tiles.RotationSwitchTileService;
import com.asdoi.quicksettings.tiles.ScreenTimeoutTileService;
import com.asdoi.quicksettings.tiles.ShowTapsTileService;
import com.asdoi.quicksettings.tiles.TakePhotoTileService;
import com.asdoi.quicksettings.tiles.ToggleAnimationTileService;
import com.asdoi.quicksettings.tiles.UsbDebuggingTileService;
import com.asdoi.quicksettings.tiles.VibrateCallsTileService;
import com.asdoi.quicksettings.utils.SharedPreferencesUtil;
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
        servicePreferences.put("take_photo", TakePhotoTileService.class);
        servicePreferences.put("record_video", RecordVideoTileService.class);
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
        servicePreferences.put("screen_timeout", ScreenTimeoutTileService.class);
        servicePreferences.put("vibrate_calls", VibrateCallsTileService.class);
        servicePreferences.put("switch_states", DoNotDisturbSwitchTileService.class);
        servicePreferences.put("force_rotation", RotationSwitchTileService.class);
        servicePreferences.put("screen_cast", OpenCastTileService.class);
        servicePreferences.put("developer_options", OpenDeveloperOptionsTileService.class);
        servicePreferences.put("open_language", OpenLocaleTileService.class);
        servicePreferences.put("custom_app_one", OpenAppOneTileService.class);
        return servicePreferences;
    }

    public static ArrayList<Class<?>> getCustomAppServices() {
        ArrayList<Class<?>> services = new ArrayList<>();
        services.add(OpenAppOneTileService.class);
        return services;
    }

    public static ArrayMap<Class<?>, String> getCustomAppKeys() {
        ArrayMap<Class<?>, String> servicePreferences = new ArrayMap<>();
        servicePreferences.put(OpenAppOneTileService.class, SharedPreferencesUtil.CUSTOM_PACKAGE_ONE);
        return servicePreferences;
    }

    public static ArrayList<Class<?>> getSecureSettingsServices() {
        ArrayList<Class<?>> services = new ArrayList<>();
        services.add(GrayscaleTileService.class);
        services.add(UsbDebuggingTileService.class);
        services.add(FinishActivitiesTileService.class);
        services.add(AnimatorDurationTileService.class);
        services.add(ShowTapsTileService.class);
        services.add(ToggleAnimationTileService.class);
        services.add(RotationSwitchTileService.class);
        return services;
    }

    public static ArrayList<Class<?>> getModifySystemSettingsServices() {
        ArrayList<Class<?>> services = new ArrayList<>();
        services.add(AdaptiveBrightnessTileService.class);
        services.add(BrightnessTileService.class);
        services.add(ScreenTimeoutTileService.class);
        services.add(VibrateCallsTileService.class);
        return services;
    }

    public static ArrayList<Class<?>> getNotificationPolicyServices() {
        ArrayList<Class<?>> services = new ArrayList<>();
        services.add(DoNotDisturbSwitchTileService.class);
        return services;
    }

    public static ArrayList<Class<?>> getSecureSettingsAndDumpServices() {
        ArrayList<Class<?>> services = new ArrayList<>();
        services.add(DemoModeTileService.class);
        return services;
    }

    public static ArrayList<Class<?>> getSecureSettingsModifySystemServices() {
        ArrayList<Class<?>> services = new ArrayList<>();
        services.add(KeepScreenOnTileService.class);
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