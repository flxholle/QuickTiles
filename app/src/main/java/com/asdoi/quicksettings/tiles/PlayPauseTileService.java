package com.asdoi.quicksettings.tiles;

import android.content.Context;
import android.media.AudioManager;
import android.view.KeyEvent;

public class PlayPauseTileService extends QuickSettingsTileService {

    @Override
    public void onClick() {
        //https://stackoverflow.com/questions/20856706/how-to-control-currently-playing-music-player-in-android/30730295#30730295
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        KeyEvent downEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
        am.dispatchMediaKeyEvent(downEvent);

        KeyEvent upEvent = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
        am.dispatchMediaKeyEvent(upEvent);
    }
}
