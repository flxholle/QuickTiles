package com.asdoi.quicksettings.intent_tiles;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import com.asdoi.quicksettings.abstract_tiles.BaseTileService;

public class OpenInputTileService extends BaseTileService {

    @Override
    public void onClick() {
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        im.showInputMethodPicker();
    }

    @Override
    public void reset() {
    }
}
