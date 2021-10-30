package edu.nmhu.bssd5250.videoview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.replace
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.commit

private const val VID_TAG:String = "video"
private const val LLID:Int = 123 // constant id for linear layout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val ll = LinearLayoutCompat(this).apply {
            orientation = LinearLayoutCompat.VERTICAL
            layoutParams = LinearLayoutCompat.LayoutParams(
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT
            )
            id = LLID
        }
        setContentView(ll)

        if (savedInstanceState == null) {
            val path1 = "android.resource://$packageName/raw/undock"
            val path2 = "android.resource://$packageName/raw/small"

            val paths: Array<String> =
                arrayOf(
                    path1,
                    path2
                )

            supportFragmentManager.commit {
                replace(ll.id, VideoViewFragment.newInstance(paths), VID_TAG)
            }
        }else{
            val stepFragment = supportFragmentManager.findFragmentByTag(VID_TAG) as VideoViewFragment
            supportFragmentManager.commit {
                replace(ll.id, stepFragment, VID_TAG)
            }
        }
    }
}