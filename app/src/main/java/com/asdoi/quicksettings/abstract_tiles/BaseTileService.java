package com.asdoi.quicksettings.abstract_tiles;

import android.service.quicksettings.TileService;

public abstract class BaseTileService extends TileService {
    public abstract void reset();

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        reset();
//    }
}
