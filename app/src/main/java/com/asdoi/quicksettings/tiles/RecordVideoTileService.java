package com.asdoi.quicksettings.tiles;

import android.content.Intent;
import android.provider.MediaStore;

import com.asdoi.quicksettings.utils.IntentTileService;

public class RecordVideoTileService extends IntentTileService {

    @Override
    public Intent createIntent() {
        return new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
    }
}
