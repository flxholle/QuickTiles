package com.flxholle.quicktiles.tiles

import android.graphics.drawable.Icon
import android.media.AudioManager
import com.flxholle.quicktiles.R
import com.flxholle.quicktiles.abstract_tiles.SelectionTileService

class MediaVolumeTileService : SelectionTileService<Int>() {
    companion object {
        const val SETTING = AudioManager.STREAM_MUSIC

        const val MUTED = 0
        const val LOUD = 1
    }

    override fun isActive(value: Int): Boolean {
        return value != MUTED
    }

    override fun queryValue(): Int {
        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        return if (audioManager.getStreamVolume(SETTING) == 0)
            MUTED
        else
            LOUD
    }

    override fun reset() {
        saveValue(LOUD)
    }

    override fun saveValue(value: Int): Boolean {
        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_TOGGLE_MUTE, AudioManager.FLAG_SHOW_UI)
        return true
    }

    override fun getValueList(): List<Int> {
        return listOf(MUTED, LOUD)
    }

    override fun updateIcon() = false

    override fun getIcon(value: Int): Icon {
        return Icon.createWithResource(applicationContext, R.drawable.ic_music_video)
    }

    override fun updateLabel() = false

    override fun getLabel(value: Int): CharSequence {
        return getString(R.string.media_volume)
    }

}