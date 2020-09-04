package com.asdoi.quicksettings.Utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.provider.Settings.Secure;
import android.widget.Toast;

import com.asdoi.quicksettings.BuildConfig;
import com.asdoi.quicksettings.R;

import java.io.DataOutputStream;

public class GrayscaleServiceUtil {
    private static final String PERMISSION = "android.permission.WRITE_SECURE_SETTINGS";
    private static final String COMMAND = "adb shell pm grant " + BuildConfig.APPLICATION_ID + " " + PERMISSION;
    private static final String SU_COMMAND = "pm grant " + BuildConfig.APPLICATION_ID + " " + PERMISSION;

    private static final String DISPLAY_DALTONIZER_ENABLED = "accessibility_display_daltonizer_enabled";
    private static final String DISPLAY_DALTONIZER = "accessibility_display_daltonizer";

    public static boolean hasPermission(Context context) {
        return context.checkCallingOrSelfPermission(PERMISSION) == PackageManager.PERMISSION_GRANTED;
    }

    public static Dialog createTipsDialog(final Context context) {
        return new AlertDialog.Builder(context)
                .setTitle(R.string.require_permission)
                .setIcon(R.drawable.ic_grayscale)
                .setMessage(context.getString(R.string.require_permission_description, COMMAND))
                .setNegativeButton(R.string.ok, null)
                .setPositiveButton(R.string.copy_text, (dialog, which) -> {
                    ClipData clipData = ClipData.newPlainText(COMMAND, COMMAND);
                    ClipboardManager manager = (ClipboardManager) context.getSystemService(Service.CLIPBOARD_SERVICE);
                    manager.setPrimaryClip(clipData);
                    Toast.makeText(context, R.string.copy_success, Toast.LENGTH_SHORT).show();
                })
                .setNeutralButton(R.string.root, (dialog, which) -> {
                    try {
                        Process su = Runtime.getRuntime().exec("su");
                        DataOutputStream os = new DataOutputStream(su.getOutputStream());
                        os.writeBytes(SU_COMMAND + "\n");
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

    public static boolean isGreyscaleEnable(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        return Secure.getInt(contentResolver, DISPLAY_DALTONIZER_ENABLED, 0) == 1
                && Secure.getInt(contentResolver, DISPLAY_DALTONIZER, 0) == 0;
    }

    public static void toggleGreyscale(Context context, boolean greyscale) {
        ContentResolver contentResolver = context.getContentResolver();
        Secure.putInt(contentResolver, DISPLAY_DALTONIZER_ENABLED, greyscale ? 1 : 0);
        Secure.putInt(contentResolver, DISPLAY_DALTONIZER, greyscale ? 0 : -1);
    }
}
