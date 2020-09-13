package com.asdoi.quicksettings.tiles;

import com.asdoi.quicksettings.abstract_tiles.OpenCustomAppTileService;
import com.asdoi.quicksettings.utils.SharedPreferencesUtil;

public class OpenAppThreeTileService extends OpenCustomAppTileService {
    @Override
    public String getPreferencesKey() {
        return SharedPreferencesUtil.CUSTOM_PACKAGE_THREE;
    }
}
