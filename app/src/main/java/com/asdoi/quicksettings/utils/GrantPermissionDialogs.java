package com.asdoi.quicksettings.utils;

import android.app.Dialog;
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
import androidx.appcompat.view.ContextThemeWrapper;

import com.asdoi.quicksettings.BuildConfig;
import com.asdoi.quicksettings.R;

import java.io.DataOutputStream;

public class GrantPermissionDialogs {
    public static final String ANDROID_PERMISSION_WRITE_SECURE_SETTINGS = "android.permission.WRITE_SECURE_SETTINGS";
    private static final String WRITE_SECURE_SETTINGS_COMMAND = "adb shell pm grant " + BuildConfig.APPLICATION_ID + " " + ANDROID_PERMISSION_WRITE_SECURE_SETTINGS;
    private static final String WRITE_SECURE_SETTINGS_SU_COMMAND = "pm grant " + BuildConfig.APPLICATION_ID + " " + ANDROID_PERMISSION_WRITE_SECURE_SETTINGS;

    public static Dialog getModifySystemSettingsDialog(final Context context) {
        return new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.myDialog))
                .setCancelable(true)
                .setTitle(R.string.require_permission)
                .setMessage(R.string.permission_alert_dialog_message)
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss())
                .setPositiveButton(R.string.settings, (dialog, which) -> {
                    context.startActivity(new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            .setData(Uri.parse("package:" + context.getPackageName())));
                })
                .create();
    }

    public static boolean hasModifySystemSettingsPermission(Context context) {
        return Settings.System.canWrite(context);
    }

    private static Dialog getSystemPermissionDialog(final Context context, final String adbCommand, final String rootCommand) {
        return new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.myDialog))
                .setCancelable(true)
                .setTitle(R.string.require_permission)
                .setMessage(context.getString(R.string.require_permission_description, adbCommand))
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

    public static Dialog getWriteSecureSettingsDialog(final Context context) {
        return getSystemPermissionDialog(context, WRITE_SECURE_SETTINGS_COMMAND, WRITE_SECURE_SETTINGS_SU_COMMAND);
    }

    public static boolean hasWriteSecureSettingsPermission(Context context) {
        return context.checkCallingOrSelfPermission(GrantPermissionDialogs.ANDROID_PERMISSION_WRITE_SECURE_SETTINGS) == PackageManager.PERMISSION_GRANTED;
    }
}
