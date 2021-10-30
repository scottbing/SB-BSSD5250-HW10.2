package edu.nmhu.bssd5250.videoview

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.VideoView
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.Fragment
import kotlin.math.abs


private const val VIDEO_PATH = "video_path"
private const val SWIPE_MIN_DISTANCE = 120
private const val SWIPE_THRESHOLD_VELOCITY = 200

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
                @SuppressLint("ClickableViewAccessibility")
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
            videoView.setVideoPath(videoPath?.get(videoIndex))
            Log.d("Long Press", videoIndex.toString())
        }

        override fun onFling(
            e1: MotionEvent, e2: MotionEvent,
            velocityX: Float, velocityY: Float
        ): Boolean {
            var left = 0.5F
            var right = 0.5F
            val amt = 10000
            var pos = videoView.currentPosition

            // horizontal swipe
            if(velocityX < 0) {
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
            /*//taken from: https://stackoverflow.com/questions/4098198/adding-fling-gesture-to-an-image-view-android
            // Horizontal Swipe
            if(e1.x - e2.x > SWIPE_MIN_DISTANCE && abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                pos -= amt
                return false // Right to left
            }  else if (e2.x - e1.x > SWIPE_MIN_DISTANCE && abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                pos += amt
                return false // Left to right
            }
*/
            // Vertical Swipe
            if(e1.y - e2.y > SWIPE_MIN_DISTANCE && abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                left += 0.2F
                right += 0.2F
                videosMP?.setVolume(left, right)
                return false // Bottom to top
            }  else if (e2.y - e1.y > SWIPE_MIN_DISTANCE && abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                left -= 0.2F
                right -= 0.2F
                videosMP?.setVolume(left, right)
                return false // Top to bottom
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