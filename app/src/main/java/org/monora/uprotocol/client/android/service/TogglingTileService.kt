package org.monora.uprotocol.client.android.service

import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import org.monora.uprotocol.client.android.backend.Backend
import javax.inject.Inject

/**
 * created by: Veli
 * date: 10.10.2017 07:58
 */
@RequiresApi(api = Build.VERSION_CODES.N)
@AndroidEntryPoint
class TogglingTileService : TileService() {
    @Inject
    lateinit var backend: Backend

    private val observer = Observer<Boolean> { activated ->
        qsTile?.let { tile ->
            tile.state = if (activated) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
            tile.updateTile()
        }
    }

    override fun onStartListening() {
        super.onStartListening()
        backend.tileState.observeForever(observer)
    }

    override fun onStopListening() {
        super.onStopListening()
        backend.tileState.removeObserver(observer)
    }

    override fun onClick() {
        super.onClick()
        backend.takeBgServiceFgThroughTogglingTile()
    }
}