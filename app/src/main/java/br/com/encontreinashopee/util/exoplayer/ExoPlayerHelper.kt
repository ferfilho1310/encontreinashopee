package br.com.encontreinashopee.util.exoplayer

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView

object ExoPlayerHelper {

    @OptIn(UnstableApi::class)
    @SuppressLint("OpaqueUnitKey")
    @Composable
    fun CustomPlayer(uri: String) {

        val visible = remember { mutableStateOf(true) }
        val context = LocalContext.current
        val videoTitle = remember { mutableStateOf("Cortador") }

        val mediaItems = arrayListOf<MediaItem>()
        mediaItems.add(
            MediaItem.Builder()
                .setUri(uri)
                .setMediaMetadata(
                    MediaMetadata.Builder().setDisplayTitle("Cortador").build()
                )
                .build()
        )

        val exoPlayer = remember {
            ExoPlayer.Builder(context).build().apply {
                this.setMediaItems(mediaItems)
                this.prepare()
                this.addListener(object : Player.Listener {
                    override fun onEvents(player: Player, events: Player.Events) {
                        super.onEvents(player, events)

                        if (player.contentPosition >= 200) visible.value = true
                    }

                    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                        super.onMediaItemTransition(mediaItem, reason)
                        visible.value = true
                        videoTitle.value = mediaItem?.mediaMetadata?.displayTitle.toString()
                    }
                })
            }
        }

        exoPlayer.playWhenReady = false

        LocalLifecycleOwner.current.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_START -> Unit
                    Lifecycle.Event.ON_STOP -> {
                        exoPlayer.pause()
                    }

                    else -> Unit
                }
            }
        })

        DisposableEffect(
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(550.dp)
                    .padding(top = 12.dp, bottom = 1.dp),
                factory = {
                    PlayerView(context).apply {
                        player = exoPlayer
                        layoutParams = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )

                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL

                        setShowNextButton(false)
                        setShowPreviousButton(false)
                        setShowFastForwardButton(false)
                        setShowRewindButton(false)
                    }
                })
        ) {
            onDispose {
                exoPlayer.release()
            }
        }
    }
}