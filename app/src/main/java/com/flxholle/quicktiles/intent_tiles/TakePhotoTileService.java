package com.flxholle.quicktiles.intent_tiles;

import android.content.Intent;
import android.provider.MediaStore;

import com.flxholle.quicktiles.abstract_tiles.IntentTileService;

public class TakePhotoTileService extends IntentTileService {

    @Override
    public Intent createIntent() {
        return new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    }
}
