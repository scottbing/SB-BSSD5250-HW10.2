package edu.nmhu.bssd5250.videoview

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.VideoView
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.Fragment
import kotlinx.coroutines.NonCancellable.start


private const val VIDEO_PATH = "video_path"

class VideoViewFragment : Fragment() {

    private var videoPath: Array<String>? = null
    private var videoIndex:Int = 0
    private var videosMP:MediaPlayer? = null
    private lateinit var videoView: VideoView
    private lateinit var mDetector: GestureDetectorCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            videoPath = it.getStringArray(VIDEO_PATH)
        }
        mDetector = GestureDetectorCompat(context, VideoGestureListener())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        videoView = VideoView(context).apply {
            Log.d("videoView Long Press", videoIndex.toString())
            setVideoPath(videoPath?.get(videoIndex))
            start()
            setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                    mDetector.onTouchEvent(p1)
                    return true
                }
            })
            setOnPreparedListener(object:MediaPlayer.OnPreparedListener{
                override fun onPrepared(p0: MediaPlayer?) {
                    videosMP = p0
                    videosMP?.setVolume(0.5F,0.5F)
                }
            })
        }
        return videoView
    }

    private inner class VideoGestureListener : GestureDetector.SimpleOnGestureListener() {

        val audioManager = context!!.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            Log.d("Double Tap", "Double Tap")
            if(videoView.isPlaying){
                videoView.pause()
            }else{
                videoView.start()
            }
            return super.onDoubleTap(e)
        }

        override fun onLongPress(e: MotionEvent?) {
            super.onLongPress(e)
            videoIndex = if (videoIndex == 0 ) 1 else 0 //flip videos
            Log.d("Long Press", videoIndex.toString())
        }

        override fun onFling(
            event1: MotionEvent, event2: MotionEvent,
            velocityX: Float, velocityY: Float
        ): Boolean {
            var pos = videoView.currentPosition

            // horizontal swipe
            if(velocityX < 0) {
                val amt = 20000
                pos -= amt
                if(pos < 0) {
                    pos = 0
                } else {
                    pos += amt
                    Log.d("onFling: velocityX", pos.toString())
                }
                videoView.seekTo(pos)
                return true
            }

            // up down
            if(velocityY < 0) {
                 videosMP?.setVolume(0.5F,0.5F)
                audioManager.adjustVolume(AudioManager.ADJUST_RAISE, 0)
                val amt = 20000
                pos -= amt

                if(pos < 0) {
                    pos = 0
                } else {
                    pos += amt
                    audioManager.adjustVolume(AudioManager.ADJUST_LOWER, 0)
                    Log.d("onFling: velocityY", pos.toString())
                }
                videoView.seekTo(pos)
                return true
            }
            return true
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(paths: Array<String>) =
            VideoViewFragment().apply {
                arguments = Bundle().apply {
                    putStringArray(VIDEO_PATH, paths)
                }
            }
    }
}