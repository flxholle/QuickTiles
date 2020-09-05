package com.asdoi.quicksettings.tiles;

import android.content.Intent;
import android.provider.MediaStore;

import com.asdoi.quicksettings.utils.IntentTileService;

public class OpenCameraTileService extends IntentTileService {

    @Override
    public Intent createIntent() {
        return new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    }
}
