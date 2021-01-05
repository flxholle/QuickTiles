package com.asdoi.quicksettings.intent_tiles;

import com.asdoi.quicksettings.abstract_tiles.OpenCustomAppTileService;
import com.asdoi.quicksettings.utils.SharedPreferencesUtil;

public class OpenAppOneTileService extends OpenCustomAppTileService {
    @Override
    public String getPreferencesKey() {
        return SharedPreferencesUtil.CUSTOM_PACKAGE_ONE;
    }
}
