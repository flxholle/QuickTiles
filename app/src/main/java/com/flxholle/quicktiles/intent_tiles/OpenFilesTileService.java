package com.flxholle.quicktiles.intent_tiles;

import android.content.Intent;
import android.os.Build;
import android.provider.DocumentsContract;
import android.service.quicksettings.Tile;

import com.flxholle.quicktiles.R;
import com.flxholle.quicktiles.abstract_tiles.IntentTileService;

import java.io.File;

public class OpenFilesTileService extends IntentTileService {

    public String getAvailableInternalMemorySize() {
        double availableSize = new File(getFilesDir().getAbsoluteFile().toString()).getFreeSpace();
        double freeSpace = availableSize / (1000 * 1000 * 1000);
        double freeSpaceRounded = ((int) Math.round(freeSpace * 100)) / 100d;
        return freeSpaceRounded + " GB";
    }

    @Override
    public Intent createIntent() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType(DocumentsContract.Document.MIME_TYPE_DIR);
        return intent;
    }

    @Override
    public void onStartListening() {
        Tile tile = getQsTile();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            tile.setLabel(getString(R.string.open_files));
            tile.setSubtitle(getString(R.string.free_space) + ": " + getAvailableInternalMemorySize());
        } else {
            tile.setLabel(getString(R.string.free_space) + ": " + getAvailableInternalMemorySize());
        }
        tile.updateTile();
    }
}
