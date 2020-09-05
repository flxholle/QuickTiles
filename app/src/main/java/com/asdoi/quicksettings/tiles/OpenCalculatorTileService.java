package com.asdoi.quicksettings.tiles;

import android.content.Intent;

import com.asdoi.quicksettings.utils.IntentTileService;

public class OpenCalculatorTileService extends IntentTileService {

    @Override
    public Intent createIntent() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_APP_CALCULATOR);
        return intent;
    }
}
