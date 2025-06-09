package com.flxholle.quicktiles;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.service.quicksettings.TileService;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.ArrayMap;
import android.util.Pair;
import android.util.TypedValue;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroup;
import androidx.preference.SwitchPreferenceCompat;

import com.bytehamster.lib.preferencesearch.SearchConfiguration;
import com.bytehamster.lib.preferencesearch.SearchPreference;
import com.flxholle.quicktiles.utils.GrantPermissionDialogs;
import com.flxholle.quicktiles.utils.SelectApp;
import com.flxholle.quicktiles.utils.SharedPreferencesUtil;
import com.mikepenz.aboutlibraries.LibsBuilder;

import java.util.ArrayList;
import java.util.Map;

public class SettingsFragment extends PreferenceFragmentCompat {
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

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        tintIcons(getPreferenceScreen(), getThemeColor(android.R.attr.textColorPrimary));

        setSwitchPreferences();

        SearchPreference searchPreference = findPreference("searchPreference");
        SearchConfiguration config = searchPreference.getSearchConfiguration();
        config.setActivity((AppCompatActivity) getActivity());
        config.index(R.xml.root_preferences);

        Preference credits = findPreference("credits");
        credits.setOnPreferenceClickListener((view) -> {
            Dialog dialog = new AlertDialog.Builder(requireContext())
                    .setTitle(R.string.credits)
                    .setMessage(Html.fromHtml(getString(R.string.credits_description), Html.FROM_HTML_MODE_LEGACY))
                    .setPositiveButton(R.string.ok, (button, listener) -> {
                    })
                    .show();
            ((TextView) dialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
            return true;
        });

        Preference libraries = findPreference("libraries");
        libraries.setOnPreferenceClickListener((view) -> {
            new LibsBuilder()
                    .withActivityTitle(getString(R.string.open_source_libraries))
                    .withAboutIconShown(true)
//                    .withFields(R.string.class.getFields())
                    .withLicenseShown(true)
                    .withAboutAppName(getString(R.string.app_name))
                    .start(requireContext());
            return true;
        });

        Preference resetCounter = findPreference("reset_counter");
        resetCounter.setOnPreferenceClickListener((preference -> {
            SharedPreferencesUtil.resetCounter(requireContext());
//            TileService.requestListeningState(requireContext(), new ComponentName(requireContext(), CounterTileService.class));
            return true;
        }));
    }

    private void setSwitchPreferences() {
        ArrayMap<String, Class<?>> preferencesServices = SettingsActivity.getPreferenceService();
        ArrayList<Class<?>> secureSettingsServices = SettingsActivity.getSecureSettingsServices();
        ArrayList<Class<?>> modifySystemSettingsServices = SettingsActivity.getModifySystemSettingsServices();
        ArrayList<Class<?>> secureSettingsDumpServices = SettingsActivity.getSecureSettingsAndDumpServices();
        ArrayList<Class<?>> rootServices = SettingsActivity.getRootServices();
        ArrayList<Class<?>> notificationPolicyServices = SettingsActivity.getNotificationPolicyServices();
        ArrayList<Class<?>> secureSettingsModifySystemServices = SettingsActivity.getSecureSettingsModifySystemServices();
        ArrayMap<Class<?>, String> selectApplicationServices = SettingsActivity.getCustomAppKeys();
        ArrayList<Class<?>> accessibilityServiceServices = SettingsActivity.getAccessibilityServiceServices();
        ArrayMap<Class<?>, Pair<Boolean, Integer>> higherMinApiServices = SettingsActivity.getHigherThanMinAPIKeys();

        for (Map.Entry<String, Class<?>> entry : preferencesServices.entrySet()) {
            SwitchPreferenceCompat switchPreference = findPreference(entry.getKey());
            if (switchPreference != null) {
                final Class<?> serviceClass = entry.getValue();

                if (higherMinApiServices.containsKey(serviceClass)) {
                    Pair<Boolean, Integer> apiValue = higherMinApiServices.get(serviceClass);
                    if (apiValue.first && Build.VERSION.SDK_INT < apiValue.second) {
                        switchPreference.setEnabled(false);
                        switchPreference.setVisible(false);
                        continue;
                    } else if (!apiValue.first && Build.VERSION.SDK_INT >= apiValue.second) {
                        switchPreference.setEnabled(false);
                        switchPreference.setVisible(false);
                        continue;
                    }
                }

                if (secureSettingsServices.contains(serviceClass)) {
                    switchPreference.setOnPreferenceChangeListener(getSecureSettingsListener(serviceClass));
                } else if (modifySystemSettingsServices.contains(serviceClass)) {
                    switchPreference.setOnPreferenceChangeListener(getModifySystemSettingsListener(serviceClass));
                } else if (secureSettingsDumpServices.contains(serviceClass)) {
                    switchPreference.setOnPreferenceChangeListener(getSecureSettingsDumpListener(serviceClass));
                } else if (rootServices.contains(serviceClass)) {
                    switchPreference.setOnPreferenceChangeListener(getRootListener(serviceClass));
                } else if (selectApplicationServices.containsKey(serviceClass)) {
                    setSelectApplicationPreferences(switchPreference, serviceClass, selectApplicationServices);
                } else if (notificationPolicyServices.contains(serviceClass)) {
                    switchPreference.setOnPreferenceChangeListener(getNotificationPolicyListener(serviceClass));
                } else if (secureSettingsModifySystemServices.contains(serviceClass)) {
                    switchPreference.setOnPreferenceChangeListener(getSecureSettingsModifySystemListener(serviceClass));
                } else if (accessibilityServiceServices.contains(serviceClass)) {
                    switchPreference.setOnPreferenceChangeListener(getAccessibilityServiceListener(serviceClass));
                } else {
                    switchPreference.setOnPreferenceChangeListener(getDefaultChangeListener(serviceClass));
                }
            }
        }
    }

    private Preference.OnPreferenceChangeListener getDefaultChangeListener(Class<?> serviceClass) {
        return (preference, newValue) -> {
            setComponentState(newValue, serviceClass);
            return true;
        };
    }

    private void setSelectApplicationPreferences(SwitchPreferenceCompat switchPreference, Class<?> serviceClass, ArrayMap<Class<?>, String> customAppKeys) {
        final String key = customAppKeys.get(serviceClass);

        String customPackageAtStart = SharedPreferencesUtil.getCustomPackage(requireContext(), key);
        if (customPackageAtStart != null)
            switchPreference.setTitle(SharedPreferencesUtil.getCustomPackage(requireContext(), key + "_label"));

        switchPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            String customPackageNew = SharedPreferencesUtil.getCustomPackage(requireContext(), key);

            if (newValue.equals(Boolean.TRUE) && customPackageNew == null) {
                SelectApp.selectApps(requireContext(), key, () -> {
                    String customPackageUpdated = SharedPreferencesUtil.getCustomPackage(requireContext(), key);
                    String preselection = SelectApp.getApplicationInfo(requireContext(), customPackageUpdated).loadLabel(requireContext().getPackageManager()).toString();

                    String labelKey = key + "_label";
                    SelectApp.insertCustomAppName(requireContext(), labelKey, preselection, () -> {
                        setComponentState(Boolean.TRUE, serviceClass);
                    TileService.requestListeningState(requireContext(), new ComponentName(requireContext(), serviceClass));
                        ((SwitchPreferenceCompat) preference).setChecked(true);
                        preference.setTitle(SharedPreferencesUtil.getCustomPackage(requireContext(), labelKey));
                    }).show();
                }).show();
                return false;
            } else {
                SharedPreferencesUtil.setCustomPackage(requireContext(), key, null);
                setComponentState(Boolean.FALSE, serviceClass);
                switchPreference.setTitle(R.string.custom_app);
                return true;
            }
        });
    }

    private Preference.OnPreferenceChangeListener getSecureSettingsListener(Class<?> serviceClass) {
        return (preference, newValue) -> {
            if (GrantPermissionDialogs.hasWriteSecureSettingsPermission(requireContext())) {
                setComponentState(newValue, serviceClass);
            } else if (newValue.equals(Boolean.TRUE)) {
                setComponentState(Boolean.FALSE, serviceClass);
                GrantPermissionDialogs.getWriteSecureSettingsDialog(requireContext()).show();
                return false;
            }
            return true;
        };
    }

    private Preference.OnPreferenceChangeListener getModifySystemSettingsListener(Class<?> serviceClass) {
        return (preference, newValue) -> {
            if (GrantPermissionDialogs.hasModifySystemSettingsPermission(requireContext())) {
                setComponentState(newValue, serviceClass);
            } else if (newValue.equals(Boolean.TRUE)) {
                setComponentState(Boolean.FALSE, serviceClass);
                GrantPermissionDialogs.getModifySystemSettingsDialog(requireContext()).show();
                return false;
            }
            return true;
        };
    }

    private Preference.OnPreferenceChangeListener getNotificationPolicyListener(Class<?> serviceClass) {
        return (preference, newValue) -> {
            if (GrantPermissionDialogs.hasNotificationPolicyPermission(requireContext())) {
                setComponentState(newValue, serviceClass);
            } else if (newValue.equals(Boolean.TRUE)) {
                setComponentState(Boolean.FALSE, serviceClass);
                GrantPermissionDialogs.getNotificationPolicyDialog(requireContext()).show();
                return false;
            }
            return true;
        };
    }

    private Preference.OnPreferenceChangeListener getSecureSettingsDumpListener(Class<?> serviceClass) {
        return (preference, newValue) -> {
            if (GrantPermissionDialogs.hasWriteSecureSettingsPermission(requireContext())
                    && GrantPermissionDialogs.hasDumpPermission(requireContext())) {
                setComponentState(newValue, serviceClass);
            } else if (newValue.equals(Boolean.TRUE)) {
                setComponentState(Boolean.FALSE, serviceClass);
                if (!GrantPermissionDialogs.hasWriteSecureSettingsPermission(requireContext()) && !GrantPermissionDialogs.hasDumpPermission(requireContext()))
                    GrantPermissionDialogs.getWriteSecureSettingsAndDumpDialog(requireContext()).show();
                else if (!GrantPermissionDialogs.hasDumpPermission(requireContext()))
                    GrantPermissionDialogs.getDumpDialog(requireContext()).show();
                else
                    GrantPermissionDialogs.getWriteSecureSettingsDialog(requireContext()).show();
                return false;
            }
            return true;
        };
    }

    private Preference.OnPreferenceChangeListener getRootListener(Class<?> serviceClass) {
        return (preference, newValue) -> {
            if (newValue.equals(Boolean.FALSE)) {
                setComponentState(newValue, serviceClass);
                return true;
            }
            if (!GrantPermissionDialogs.hasRootPermission()) {
                setComponentState(Boolean.FALSE, serviceClass);
                GrantPermissionDialogs.getRootPermissionDialog(requireContext()).show();
                return false;
            }
            setComponentState(newValue, serviceClass);
            return true;
        };
    }

    private Preference.OnPreferenceChangeListener getSecureSettingsModifySystemListener(Class<?> serviceClass) {
        return (preference, newValue) -> {
            if (GrantPermissionDialogs.hasModifySystemSettingsPermission(requireContext())
                    && GrantPermissionDialogs.hasWriteSecureSettingsPermission(requireContext())) {
                setComponentState(newValue, serviceClass);
            } else if (newValue.equals(Boolean.TRUE)) {
                setComponentState(Boolean.FALSE, serviceClass);
                if (!GrantPermissionDialogs.hasModifySystemSettingsPermission(requireContext()))
                    GrantPermissionDialogs.getModifySystemSettingsDialog(requireContext()).show();
                else
                    GrantPermissionDialogs.getWriteSecureSettingsDialog(requireContext()).show();
                return false;
            }
            return true;
        };
    }

    private Preference.OnPreferenceChangeListener getAccessibilityServiceListener(Class<?> serviceClass) {
        return (preference, newValue) -> {
            if (GrantPermissionDialogs.hasAccessibilityServicePermission(requireContext())) {
                setComponentState(newValue, serviceClass);
            } else if (newValue.equals(Boolean.TRUE)) {
                setComponentState(Boolean.FALSE, serviceClass);
                GrantPermissionDialogs.getAccessibilityServiceDialog(requireContext()).show();
                return false;
            }
            return true;
        };
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

