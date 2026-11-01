/*
 * Copyright (C) 2017 Adrián García
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.flxholle.quicktiles.abstract_tiles

import android.graphics.drawable.Icon
import android.service.quicksettings.Tile
import android.util.Log
import androidx.annotation.CallSuper

abstract class SelectionTileService<T : Any> : BaseTileService() {

    final override fun onStartListening() {
        updateState()
    }

    @CallSuper
    override fun onTileClicked() {
        setNextValue()
    }

    private fun setNextValue() {
        // The current value is read from the system on every click instead of being cached in a
        // field. A TileService is created on demand and Android destroys it again as soon as the
        // Quick Settings panel is closed, so no in-memory state survives between two clicks.
        // Relying on a cached value made every tile throw (and therefore do nothing) once the
        // app was no longer running in the background.
        val currentValue = queryCurrentValue() ?: return
        val valueList = getValueList()

        if (valueList.isEmpty()) {
            Log.w(BaseTileService.LOG_TAG, "No values configured for ${javaClass.simpleName}")
            return
        }

        val newIndex = (valueList.indexOf(currentValue) + 1) % valueList.size
        val newValue = valueList[newIndex]

        if (checkPermission()) {
            try {
                saveValue(newValue)
            } catch (e: Exception) {
                Log.e(BaseTileService.LOG_TAG, "Could not save new value for ${javaClass.simpleName}", e)
            }
        }

        updateState()
    }

    open fun checkPermission(): Boolean {
        return true
    }

    /**
     * Reads the value that is currently stored in the system. Returns null (and logs the
     * problem) instead of crashing the tile service if the system cannot be queried.
     */
    private fun queryCurrentValue(): T? {
        return try {
            queryValue()
        } catch (e: Exception) {
            Log.e(BaseTileService.LOG_TAG, "Could not read value of ${javaClass.simpleName}", e)
            null
        }
    }

    private fun updateState() {
        val tile = getTileOrNull() ?: return
        val value = queryCurrentValue() ?: return

        try {
            tile.state = if (isActive(value)) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
            if (updateLabel())
                tile.label = getLabel(value)
            if (updateIcon())
                tile.icon = getIcon(value)

            tile.updateTile()
        } catch (e: Exception) {
            Log.e(BaseTileService.LOG_TAG, "Could not update tile of ${javaClass.simpleName}", e)
        }
    }

    abstract fun isActive(value: T): Boolean

    abstract fun getValueList(): List<T>

    abstract fun queryValue(): T

    abstract fun saveValue(value: T): Boolean

    abstract fun getIcon(value: T): Icon

    abstract fun getLabel(value: T): CharSequence

    open fun updateLabel(): Boolean {
        return true
    }

    open fun updateIcon(): Boolean {
        return true
    }
}
