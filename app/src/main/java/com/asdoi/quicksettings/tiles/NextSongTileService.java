package com.asdoi.quicksettings.tiles;

import android.content.Context;
import android.media.AudioManager;
import android.service.quicksettings.TileService;
import android.view.KeyEvent;

public class NextSongTileService extends TileService {

    @Override
    public void onClick() {
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        KeyEvent downEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_NEXT);
        am.dispatchMediaKeyEvent(downEvent);

        KeyEvent upEvent = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_NEXT);
        am.dispatchMediaKeyEvent(upEvent);
    }
}
