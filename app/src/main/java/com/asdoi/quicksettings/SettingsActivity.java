package com.asdoi.quicksettings;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.ArrayMap;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.asdoi.quicksettings.tiles.MediaVolumeTileService;
import com.asdoi.quicksettings.tiles.NextSongTileService;
import com.asdoi.quicksettings.tiles.PlayPauseTileService;
import com.asdoi.quicksettings.tiles.PreviousSongTileService;
import com.bytehamster.lib.preferencesearch.SearchPreferenceResult;
import com.bytehamster.lib.preferencesearch.SearchPreferenceResultListener;
import com.mikepenz.aboutlibraries.LibsBuilder;

public class SettingsActivity extends AppCompatActivity implements SearchPreferenceResultListener {
    private SettingsFragment prefsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        TextView credits = findViewById(R.id.credits);
        credits.setText(Html.fromHtml(getString(R.string.credits), Html.FROM_HTML_MODE_LEGACY));
        credits.setMovementMethod(LinkMovementMethod.getInstance());

        ImageView info = findViewById(R.id.info_image);
        info.setOnClickListener((view) ->
                new LibsBuilder()
                        .withActivityTitle(getString(R.string.open_source_libraries))
                        .withAboutIconShown(true)
                        .withFields(R.string.class.getFields())
                        .withLicenseShown(true)
                        .withAboutAppName(getString(R.string.app_name))
                        .start(this));

        prefsFragment = new SettingsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, prefsFragment)
                .commit();
    }

    public static ArrayMap<String, Class<?>> getPreferenceService() {
        ArrayMap<String, Class<?>> servicePreferences = new ArrayMap<>();
        servicePreferences.put("play_pause", PlayPauseTileService.class);
        servicePreferences.put("next_song", NextSongTileService.class);
        servicePreferences.put("previous_song", PreviousSongTileService.class);
        servicePreferences.put("media_volume", MediaVolumeTileService.class);
        return servicePreferences;
    }


    @Override
    public void onSearchResultClicked(SearchPreferenceResult result) {
        result.closeSearchPage(this);
        result.highlight(prefsFragment);
    }
}