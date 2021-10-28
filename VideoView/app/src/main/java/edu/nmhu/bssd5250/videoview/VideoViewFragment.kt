package edu.nmhu.bssd5250.videoview

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.VideoView
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.Fragment

private const val VIDEO_PATH = "video_path"

class VideoViewFragment : Fragment() {

    private var videoPath: String? = null
    private var videosMP:MediaPlayer? = null
    private lateinit var videoView: VideoView
    private lateinit var mDetector: GestureDetectorCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            videoPath = it.getString(VIDEO_PATH)
        }
        mDetector = GestureDetectorCompat(context, VideoGestureListener())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        videoView = VideoView(context).apply {
            setVideoPath(videoPath)
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
                }
            })
        }
        return videoView
    }

    private inner class VideoGestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            videosMP?.setVolume(0F,0F)
            if(videoView.isPlaying){
                videoView.pause()
            }else{
                videoView.start()
            }
            return super.onDoubleTap(e)
        }

        override fun onLongPress(e: MotionEvent?) {
            super.onLongPress(e)
        }

        override fun onDown(e: MotionEvent?): Boolean {
            val event = null
            Log.d("FRAG", "onFling: $event")
            return true
        }

        override fun onFling(
            event1: MotionEvent, event2: MotionEvent,
            velocityX: Float, velocityY: Float
        ): Boolean {
            var pos = videoView.currentPosition
            // x is left to right and y would be up and down
            // a negative number for X means we are going right to left
            if(velocityX < 0) {
                val amt = 20000
                pos -= amt
                if(pos < 0) {
                    pos = 0
                } else {
                    pos += amt
                }
                videoView.seekTo(pos)
                return true
            }
//            Log.d("FRAG", "onFling: $event1 $event2")
            return true
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(path: String) =
            VideoViewFragment().apply {
                arguments = Bundle().apply {
                    putString(VIDEO_PATH, path)
                }
            }
    }
}