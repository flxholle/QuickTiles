package com.asdoi.quicksettings.intent_tiles;

import android.content.Intent;
import android.provider.MediaStore;

import com.asdoi.quicksettings.abstract_tiles.IntentTileService;

public class TakePhotoTileService extends IntentTileService {

    @Override
    public Intent createIntent() {
        return new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    }
}
