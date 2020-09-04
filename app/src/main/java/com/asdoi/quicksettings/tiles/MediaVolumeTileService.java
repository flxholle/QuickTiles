package com.asdoi.quicksettings.tiles;

import android.content.Context;
import android.media.AudioManager;
import android.service.quicksettings.TileService;

public class MediaVolumeTileService extends TileService {

    @Override
    public void onClick() {
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (am.getStreamVolume(AudioManager.STREAM_MUSIC) == 0)
            am.setStreamVolume(AudioManager.STREAM_MUSIC, am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
        else
            am.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
    }
}
