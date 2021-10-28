package edu.nmhu.bssd5250.videoview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.VideoView

private const val VIDEO_PATH = "video_path"

class VideoViewFragment : Fragment() {

    private var videoPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            videoPath = it.getString(VIDEO_PATH)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val videoView = VideoView(context).apply {
            setVideoPath(videoPath)
            start()
        }

        /*val mediaController = MediaController(context).apply {
            setAnchorView(videoView)
        }
        videoView.setMediaController(mediaController)*/

        return videoView

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