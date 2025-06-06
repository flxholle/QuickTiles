package com.flxholle.quicktiles.intent_tiles;

import android.content.Context;
import android.media.AudioManager;
import android.view.KeyEvent;

import com.flxholle.quicktiles.abstract_tiles.BaseTileService;

public class NextSongTileService extends BaseTileService {

    @Override
    public void onClick() {
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        KeyEvent downEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_NEXT);
        am.dispatchMediaKeyEvent(downEvent);

        KeyEvent upEvent = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_NEXT);
        am.dispatchMediaKeyEvent(upEvent);
    }

    @Override
    public void reset() {
    }
}
