package com.asdoi.quicksettings;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.ArrayMap;
import android.util.TypedValue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroup;
import androidx.preference.SwitchPreferenceCompat;

import com.bytehamster.lib.preferencesearch.SearchConfiguration;
import com.bytehamster.lib.preferencesearch.SearchPreference;
import com.mikepenz.aboutlibraries.LibsBuilder;

import java.util.Map;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        tintIcons(getPreferenceScreen(), getThemeColor(android.R.attr.textColorPrimary));

        ArrayMap<String, Class<?>> preferencesServices = SettingsActivity.getPreferenceService();
        for (Map.Entry<String, Class<?>> entry : preferencesServices.entrySet()) {
            SwitchPreferenceCompat switchPreference = findPreference(entry.getKey());
            if (switchPreference != null) {
                final Class<?> serviceClass = entry.getValue();
                switchPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                    setComponentState(newValue, serviceClass);
                    return true;
                });
            }
        }

        SearchPreference searchPreference = findPreference("searchPreference");
        SearchConfiguration config = searchPreference.getSearchConfiguration();
        config.setActivity((AppCompatActivity) getActivity());
        config.index(R.xml.root_preferences);

        Preference credits = findPreference("credits");
        credits.setTitle(Html.fromHtml(getString(R.string.app_icon_credit), Html.FROM_HTML_MODE_LEGACY));
        credits.setOnPreferenceClickListener((view) -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.app_icon_credit_link))));
            return true;
        });

        Preference libraries = findPreference("libraries");
        libraries.setOnPreferenceClickListener((view) -> {
            new LibsBuilder()
                    .withActivityTitle(getString(R.string.open_source_libraries))
                    .withAboutIconShown(true)
                    .withFields(R.string.class.getFields())
                    .withLicenseShown(true)
                    .withAboutAppName(getString(R.string.app_name))
                    .start(requireContext());
            return true;
        });
    }

    private void setComponentState(Object newValue, Class<?> serviceClass) {
        if (newValue.equals(Boolean.TRUE))
            enableComponent(serviceClass);
        else
            disableComponent(serviceClass);
    }


    private void disableComponent(Class<?> serviceClass) {
        PackageManager pm = requireActivity().getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(requireActivity(), serviceClass),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    private void enableComponent(Class<?> serviceClass) {
        PackageManager pm = requireActivity().getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(requireActivity(), serviceClass),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    private static void tintIcons(Preference preference, int color) {
        if (preference instanceof PreferenceGroup) {
            PreferenceGroup group = ((PreferenceGroup) preference);
            for (int i = 0; i < group.getPreferenceCount(); i++) {
                tintIcons(group.getPreference(i), color);
            }
        } else {
            Drawable icon = preference.getIcon();
            if (icon != null) {
                DrawableCompat.setTint(icon, color);
            }
        }
    }

    private int getThemeColor(int themeAttributeId) {
        try {
            TypedValue outValue = new TypedValue();
            Resources.Theme theme = requireContext().getTheme();
            boolean wasResolved = theme.resolveAttribute(themeAttributeId, outValue, true);
            if (wasResolved) {
                return ContextCompat.getColor(requireContext(), outValue.resourceId);
            } else {
                // fallback colour handling
                return Color.BLACK;
            }
        } catch (Exception e) {
            return Color.BLACK;
        }
    }
}

