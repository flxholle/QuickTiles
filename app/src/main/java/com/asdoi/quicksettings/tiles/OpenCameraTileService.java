package com.asdoi.quicksettings.tiles;

import android.content.Intent;
import android.provider.MediaStore;

import com.asdoi.quicksettings.utils.BaseTileService;

public class OpenCameraTileService extends BaseTileService {

    @Override
    public void onClick() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        Intent close_notification_bar = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        sendBroadcast(close_notification_bar);
    }

    @Override
    public void reset() {
    }
}
