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

package com.asdoi.quicksettings.abstract_tiles

import android.graphics.drawable.Icon
import android.service.quicksettings.Tile
import androidx.annotation.CallSuper

abstract class SelectionTileService<T : Any> : BaseTileService() {

    lateinit var value: T

    final override fun onStartListening() {
        value = queryValue()
        updateState()
    }

    @CallSuper
    override fun onClick() {
        setNextValue()
    }

    private fun setNextValue() {
        val newIndex = ((getValueList().indexOf(value) + 1) % getValueList().size)
        val newValue = getValueList()[newIndex]

        // Disable tile while setting the value
        qsTile.state = Tile.STATE_UNAVAILABLE
        qsTile.updateTile()

        if (checkPermission()) {
            if (saveValue(newValue)) {
                value = newValue
            }
        }

        updateState()
    }

    open fun checkPermission(): Boolean {
        return true
    }

    private fun updateState() {
        qsTile.state = if (isActive(value)) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
        if (updateLabel())
            qsTile.label = getLabel(value)
        if (updateIcon())
            qsTile.icon = getIcon(value)

        qsTile.updateTile()
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
