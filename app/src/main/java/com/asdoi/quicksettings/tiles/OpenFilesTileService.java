package com.asdoi.quicksettings.tiles;

import android.content.Intent;
import android.provider.DocumentsContract;
import android.service.quicksettings.Tile;

import com.asdoi.quicksettings.R;
import com.asdoi.quicksettings.utils.BaseTileService;

import java.io.File;

public class OpenFilesTileService extends BaseTileService {

    public String getAvailableInternalMemorySize() {
        double availableSize = new File(getFilesDir().getAbsoluteFile().toString()).getFreeSpace();
        double freeSpace = availableSize / (1000 * 1000 * 1000);
        double freeSpaceRounded = ((int) Math.round(freeSpace * 100)) / 100d;
        return freeSpaceRounded + " GB";
    }

    @Override
    public void onClick() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType(DocumentsContract.Document.MIME_TYPE_DIR);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        Intent close_notification_bar = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        sendBroadcast(close_notification_bar);
    }

    @Override
    public void onStartListening() {
        Tile tile = getQsTile();
        tile.setLabel(getString(R.string.free_space) + ": " + getAvailableInternalMemorySize());
        tile.updateTile();
    }

    @Override
    public void reset() {
    }
}
