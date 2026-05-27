package com.tarsislimadev.android.map

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.tarsislimadev.android.map.ui.theme.MapAppTheme
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().userAgentValue = packageName
        enableEdgeToEdge()
        setContent {
            MapAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    OsmMapScreen()
                }
            }
        }
    }
}

@Composable
fun OsmMapScreen() {
    val context = LocalContext.current
    val saoPaulo = remember { GeoPoint(-23.5505, -46.6333) }
    val mapView = remember(context) {
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            controller.setZoom(15.0)
            controller.setCenter(saoPaulo)

            val marker = Marker(this).apply {
                position = saoPaulo
                title = "São Paulo"
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            }
            overlays.add(marker)
        }
    }

    DisposableEffect(mapView) {
        mapView.onResume()
        onDispose {
            mapView.onPause()
        }
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { mapView },
        update = { view ->
            view.controller.setCenter(saoPaulo)
        }
    )
}