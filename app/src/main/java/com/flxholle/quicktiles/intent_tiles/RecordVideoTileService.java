package com.flxholle.quicktiles.intent_tiles;

import android.content.Intent;
import android.provider.MediaStore;

import com.flxholle.quicktiles.abstract_tiles.IntentTileService;

public class RecordVideoTileService extends IntentTileService {

    @Override
    public Intent createIntent() {
        return new Intent(MediaStore.INTENT_ACTION_VIDEO_CAMERA);
    }
}
