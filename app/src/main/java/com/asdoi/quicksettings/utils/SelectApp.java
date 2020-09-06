package com.asdoi.quicksettings.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.asdoi.quicksettings.R;

import java.util.Iterator;
import java.util.List;

public class SelectApp {
    private static List<ApplicationInfo> getApplicationList(Context context) {
        // Get installed applications
        final PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> installedApplications =
                packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        // Remove system apps
        Iterator<ApplicationInfo> it = installedApplications.iterator();
        while (it.hasNext()) {
            ApplicationInfo appInfo = it.next();
            if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                it.remove();
            }
        }

        // Return installed applications
        return installedApplications;
    }

    public static ApplicationInfo getApplicationInfo(Context context, String packageName) {
        List<ApplicationInfo> applicationInfoList = SelectApp.getApplicationList(context);
        for (ApplicationInfo appInfo : applicationInfoList) {
            if (appInfo.packageName.equals(packageName))
                return appInfo;
        }
        return context.getApplicationInfo();
    }

    public static Dialog selectApps(Context context, String key, Runnable runAfterSelection) {
        ListView applicationsList = new ListView(context);
        applicationsList.setPadding(32, 32, 32, 32);
        Dialog dialog = new AlertDialog.Builder(context)
                .setView(applicationsList)
                .setPositiveButton(null, null)
                .setNegativeButton(null, null)
                .create();
        applicationsList.setAdapter(new ApplicationAdapter(context, getApplicationList(context), dialog, key, runAfterSelection));
        return dialog;
    }

    public static Dialog selectApps(Context context, String key) {
        return selectApps(context, key, () -> {
        });
    }

    static class ApplicationAdapter extends ArrayAdapter<ApplicationInfo> {
        private List<ApplicationInfo> appsList;
        private Context context;
        private PackageManager packageManager;
        private Dialog dialog;
        private String saveKey;
        private Runnable runAfterSelection;

        public ApplicationAdapter(Context context, List<ApplicationInfo> appsList, Dialog dialog, String saveKey, Runnable runAfterSelection) {
            super(context, 0, appsList);
            this.context = context;
            this.appsList = appsList;
            packageManager = context.getPackageManager();
            this.dialog = dialog;
            this.saveKey = saveKey;
            this.runAfterSelection = runAfterSelection;
        }

        @Override
        public int getCount() {
            return ((null != appsList) ? appsList.size() : 0);
        }

        @Override
        public ApplicationInfo getItem(int position) {
            return ((null != appsList) ? appsList.get(position) : null);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (null == view) {
                LayoutInflater layoutInflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = layoutInflater.inflate(R.layout.app_listview, null);
            }

            final ApplicationInfo data = appsList.get(position);
            if (null != data) {
                TextView appName = view.findViewById(R.id.app_name);
                ImageView iconView = view.findViewById(R.id.app_icon);

                appName.setText(data.loadLabel(packageManager));
                iconView.setImageDrawable(data.loadIcon(packageManager));
            }

            view.setOnClickListener((v) -> {
                SharedPreferencesUtil.setCustomPackage(context, saveKey, data.packageName);
                runAfterSelection.run();
                dialog.dismiss();
            });
            return view;
        }
    }
}
