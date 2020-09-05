package com.asdoi.quicksettings.tiles;

import android.content.Intent;

import com.asdoi.quicksettings.utils.IntentTileService;

public class OpenVpnTileService extends IntentTileService {

    @Override
    public Intent createIntent() {
        return new Intent("android.net.vpn.SETTINGS");
    }
}
