package com.example.playermovie

import android.content.pm.ActivityInfo
import android.content.pm.ConfigurationInfo
import android.content.res.Configuration
import android.media.AudioManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView

class MainActivity : AppCompatActivity() {
    companion object{
        var isFullScreen = false
        var isLock = false
    }
    lateinit var bt_fullScreen: ImageView
    lateinit var simpleExoPlayer: SimpleExoPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val playerview = findViewById<PlayerView>(R.id.player)
        val progressbar = findViewById<ProgressBar>(R.id.progress_bar)
        bt_fullScreen = findViewById<ImageView>(R.id.bt_fullscreen)
        var bt_lockScreen = findViewById<ImageView>(R.id.exo_lock)

        bt_fullScreen.setOnClickListener {
            if(!isFullScreen){
                bt_fullScreen.setImageDrawable(ContextCompat.getDrawable(applicationContext,R.drawable.ic_baseline_fullscreen_exit))
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE)
            }else{
                bt_fullScreen.setImageDrawable(ContextCompat.getDrawable(applicationContext,R.drawable.ic_baseline_fullscreen))
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            }
            isFullScreen = !isFullScreen
        }

        bt_lockScreen.setOnClickListener {
            if(!isLock){
                bt_lockScreen.setImageDrawable(ContextCompat.getDrawable(applicationContext,R.drawable.ic_baseline_lock))
            }else{
                bt_lockScreen.setImageDrawable(ContextCompat.getDrawable(applicationContext,R.drawable.ic_baseline_lock_open))
            }
            isLock = !isLock
            lockScreen(isLock)
        }

        simpleExoPlayer = SimpleExoPlayer.Builder(this)
            .setSeekBackIncrementMs(5000)
            .setSeekForwardIncrementMs(5000)
            .build()
        playerview.player = simpleExoPlayer
        playerview.keepScreenOn = true
        simpleExoPlayer.addListener(object : Player.Listener{
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                if(playbackState == Player.STATE_BUFFERING){
                    progressbar.visibility = View.VISIBLE
                }else if(playbackState == Player.STATE_READY){
                    progressbar.visibility = View.GONE
                }
            }
        })

        var path = "android.resource://" + packageName + "/" + R.raw.video;

        val videoSource = Uri.parse(path)
        val mediaItem = MediaItem.fromUri(videoSource)
        simpleExoPlayer.setMediaItem(mediaItem)
        simpleExoPlayer.prepare()
        simpleExoPlayer.play()

    }

    private fun lockScreen(lock: Boolean) {
        val sec_mid = findViewById<LinearLayout>(R.id.sec_contrlvid1)
        val sec_bottom = findViewById<LinearLayout>(R.id.sec_controlvid2)
        if(lock){
            sec_mid.visibility = View.INVISIBLE
            sec_bottom.visibility = View.INVISIBLE
        }else{
            sec_mid.visibility = View.VISIBLE
            sec_bottom.visibility = View.VISIBLE
        }
    }
    override fun onBackPressed() {
        if(isLock) return
        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            bt_fullScreen.performClick()
        }
        else super.onBackPressed()
    }
    override fun onStop() {
        super.onStop()
        simpleExoPlayer.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        simpleExoPlayer.release()
    }

    override fun onPause() {
        super.onPause()
        simpleExoPlayer.pause()
    }
}