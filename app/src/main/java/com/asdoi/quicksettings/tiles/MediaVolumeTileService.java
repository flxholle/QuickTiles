package com.asdoi.quicksettings.tiles;

import android.content.Context;
import android.media.AudioManager;

import com.asdoi.quicksettings.utils.BaseTileService;

public class MediaVolumeTileService extends BaseTileService {

    @Override
    public void onClick() {
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (am.getStreamVolume(AudioManager.STREAM_MUSIC) == 0)
            am.setStreamVolume(AudioManager.STREAM_MUSIC, am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
        else
            am.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
    }

    @Override
    public void reset() {
    }
}
