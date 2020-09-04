package com.asdoi.quicksettings.Utils;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.asdoi.quicksettings.BuildConfig;
import com.asdoi.quicksettings.R;

import java.io.DataOutputStream;

public class GrantPermissionDialogs {
    public static final String ANDROID_PERMISSION_WRITE_SECURE_SETTINGS = "android.permission.WRITE_SECURE_SETTINGS";
    private static final String WRITE_SECURE_SETTINGS_COMMAND = "adb shell pm grant " + BuildConfig.APPLICATION_ID + " " + ANDROID_PERMISSION_WRITE_SECURE_SETTINGS;
    private static final String WRITE_SECURE_SETTINGS_SU_COMMAND = "pm grant " + BuildConfig.APPLICATION_ID + " " + ANDROID_PERMISSION_WRITE_SECURE_SETTINGS;

    private static void showModifySystemSettingsDialog(final Context context) {
        new AlertDialog.Builder(context)
                .setCancelable(true)
                .setTitle(R.string.require_permission)
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss())
                .setMessage(R.string.permission_alert_dialog_message)
                .setPositiveButton(R.string.settings, (dialog, which) -> {
                    context.startActivity(new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            .setData(Uri.parse("package:" + context.getPackageName())));
                })
                .show();
    }

    public static boolean hasModifySystemSettingsPermission(Context context) {
        return Settings.System.canWrite(context);
    }

    public static boolean checkModifySystemSettings(Context context) {
        if (hasModifySystemSettingsPermission(context)) {
            return true;
        } else {
            showModifySystemSettingsDialog(context);
            return false;
        }
    }

    private static void showWriteSecureSettingsDialog(final Context context) {
        new android.app.AlertDialog.Builder(context)
                .setTitle(R.string.require_permission)
                .setMessage(context.getString(R.string.require_permission_description, WRITE_SECURE_SETTINGS_COMMAND))
                .setNegativeButton(R.string.ok, null)
                .setPositiveButton(R.string.copy_text, (dialog, which) -> {
                    ClipData clipData = ClipData.newPlainText(WRITE_SECURE_SETTINGS_COMMAND, WRITE_SECURE_SETTINGS_COMMAND);
                    ClipboardManager manager = (ClipboardManager) context.getSystemService(Service.CLIPBOARD_SERVICE);
                    manager.setPrimaryClip(clipData);
                    Toast.makeText(context, R.string.copy_success, Toast.LENGTH_SHORT).show();
                })
                .setNeutralButton(R.string.root, (dialog, which) -> {
                    try {
                        Process su = Runtime.getRuntime().exec("su");
                        DataOutputStream os = new DataOutputStream(su.getOutputStream());
                        os.writeBytes(WRITE_SECURE_SETTINGS_SU_COMMAND + "\n");
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
                .show();
    }

    public static boolean hasWriteSecureSettingsPermission(Context context) {
        return context.checkCallingOrSelfPermission(GrantPermissionDialogs.ANDROID_PERMISSION_WRITE_SECURE_SETTINGS) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean checkWriteSecureSettingsPermission(Context context) {
        if (hasWriteSecureSettingsPermission(context)) {
            return true;
        } else {
            showWriteSecureSettingsDialog(context);
            return false;
        }
    }
}
