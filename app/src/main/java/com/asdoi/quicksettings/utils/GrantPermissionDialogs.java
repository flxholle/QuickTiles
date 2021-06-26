package com.asdoi.quicksettings.utils;

import android.Manifest;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;

import com.asdoi.quicksettings.BuildConfig;
import com.asdoi.quicksettings.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.util.List;

public class GrantPermissionDialogs {
    private static final String WRITE_SECURE_SETTINGS = Manifest.permission.WRITE_SECURE_SETTINGS;
    private static final String DUMP = Manifest.permission.DUMP;

    public static Dialog getModifySystemSettingsDialog(final Context context) {
        return new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.myDialog))
                .setCancelable(true)
                .setTitle(R.string.permission_required)
                .setMessage(R.string.permission_modify_system_settings_description)
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss())
                .setPositiveButton(R.string.settings, (dialog, which) -> {
                    context.startActivity(new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
                            .setData(Uri.parse("package:" + context.getPackageName())));
                })
                .create();
    }

    public static boolean hasModifySystemSettingsPermission(Context context) {
        return Settings.System.canWrite(context);
    }

    private static Dialog getSystemPermissionDialog(final String adbCommand, final String rootCommand, final Context context) {
        return new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.myDialog))
                .setCancelable(true)
                .setTitle(R.string.permission_required)
                .setMessage(context.getString(R.string.permission_required_description, adbCommand))
                .setNegativeButton(R.string.ok, null)
                .setPositiveButton(R.string.copy_text, (dialog, which) -> {
                    ClipData clipData = ClipData.newPlainText(adbCommand, adbCommand);
                    ClipboardManager manager = (ClipboardManager) context.getSystemService(Service.CLIPBOARD_SERVICE);
                    manager.setPrimaryClip(clipData);
                    Toast.makeText(context, R.string.copy_success, Toast.LENGTH_SHORT).show();
                })
                .setNeutralButton(R.string.root, (dialog, which) -> {
                    try {
                        Process su = Runtime.getRuntime().exec("su");
                        DataOutputStream os = new DataOutputStream(su.getOutputStream());
                        os.writeBytes(rootCommand + "\n");
                        os.writeBytes("exit\n");
                        os.close();
                        su.waitFor();
                        os.close();
                        su.destroy();
                    } catch (Exception e) {
                        Toast.makeText(context, R.string.root_failure, Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                })
                .create();
    }

    private static Dialog getSystemPermissionDialog(final Context context, final String... permission) {
        StringBuilder adbCommand = new StringBuilder("adb shell pm grant " + BuildConfig.APPLICATION_ID + " " + permission[0]);
        StringBuilder rootCommand = new StringBuilder("pm grant " + BuildConfig.APPLICATION_ID + " " + permission[0]);
        for (int i = 1; i < permission.length; i++) {
            adbCommand.append("\n")
                    .append("adb shell pm grant " + BuildConfig.APPLICATION_ID + " " + permission[1]);
            rootCommand.append("\n")
                    .append("pm grant " + BuildConfig.APPLICATION_ID + " " + permission[1]);
        }
        return getSystemPermissionDialog(adbCommand.toString(), rootCommand.toString(), context);
    }

    private static boolean hasPermission(Context context, String permission) {
        return context.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean hasWriteSecureSettingsPermission(Context context) {
        return hasPermission(context, WRITE_SECURE_SETTINGS);
    }

    public static Dialog getWriteSecureSettingsDialog(final Context context) {
        return getSystemPermissionDialog(context, WRITE_SECURE_SETTINGS);
    }

    public static boolean hasDumpPermission(Context context) {
        return hasPermission(context, DUMP);
    }

    public static Dialog getDumpDialog(final Context context) {
        return getSystemPermissionDialog(context, DUMP);
    }

    public static Dialog getWriteSecureSettingsAndDumpDialog(final Context context) {
        return getSystemPermissionDialog(context, WRITE_SECURE_SETTINGS, DUMP);
    }

    public static boolean hasNotificationPolicyPermission(Context context) {
        return ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).isNotificationPolicyAccessGranted();
    }

    public static Dialog getNotificationPolicyDialog(final Context context) {
        return new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.myDialog))
                .setCancelable(true)
                .setTitle(R.string.permission_required)
                .setMessage(R.string.permission_notification_policy_description)
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss())
                .setPositiveButton(R.string.settings, (dialog, which) -> {
                    context.startActivity(new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                })
                .create();
    }

    public static boolean hasAccessibilityServicePermission(Context context) {
        return hasAccessibilityServicePermission(context, CustomAccessibilityService.class);
    }

    public static boolean hasAccessibilityServicePermission(Context context, Class<?> accessibilityServiceClass) {
        AccessibilityManager accessibilityManager = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> accessibilityServiceInfoList;
        if (accessibilityManager != null) {
            accessibilityServiceInfoList = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK);
            for (AccessibilityServiceInfo enabledService : accessibilityServiceInfoList) {
                ServiceInfo enabledServiceInfo = enabledService.getResolveInfo().serviceInfo;
                if (enabledServiceInfo.packageName.equals(context.getPackageName()) && enabledServiceInfo.name.equals(accessibilityServiceClass.getName()))
                    return true;
            }
        }
        return false;
    }

    public static Dialog getAccessibilityServiceDialog(final Context context) {
        return new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.myDialog))
                .setCancelable(true)
                .setTitle(R.string.permission_required)
                .setMessage(context.getString(R.string.permission_accessibility_service_description, context.getString(R.string.app_name)))
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss())
                .setPositiveButton(R.string.settings, (dialog, which) -> {
                    context.startActivity(
                            new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                })
                .create();
    }

    // Found somewhere online
    public static boolean hasRootPermission() {
        boolean retval ;
        try
        {
            Process suProcess = Runtime.getRuntime().exec("su");

            DataOutputStream os = new DataOutputStream(suProcess.getOutputStream());
            BufferedReader osBr = new BufferedReader(new InputStreamReader(suProcess.getInputStream()));

            os.writeBytes("id\n");
            os.flush();
            String currUid = osBr.readLine();
            boolean exitSu = false;
            if (null == currUid)
            {
                retval = false;
                Log.d("ROOT", "Can't get root access or denied by user");
            }
            else if (currUid.contains("uid=0"))
            {
                retval = true;
                exitSu = true;
                Log.d("ROOT", "Root access granted");
            }
            else
            {
                retval = false;
                exitSu = true;
                Log.d("ROOT", "Root access rejected: " + currUid);
            }

            if (exitSu)
            {
                os.writeBytes("exit\n");
                os.flush();
            }
        }
        catch (Exception e)
        {
            retval = false;
            Log.d("ROOT", "Root access rejected [" + e.getClass().getName() + "] : " + e.getMessage());
        }

        return retval;
    }

    public static Dialog getRootPermissionDialog(final Context context) {
        return new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.myDialog))
                .setCancelable(true)
                .setTitle(R.string.permission_required)
                .setMessage(context.getString(R.string.permission_root_description))
                .setNegativeButton(R.string.ok, null)
                .setPositiveButton(R.string.root, (dialog, which) -> {
                    if(!hasRootPermission()) {
                        Toast.makeText(context, R.string.root_failure, Toast.LENGTH_SHORT).show();
                    }
                })
                .create();
    }
}
