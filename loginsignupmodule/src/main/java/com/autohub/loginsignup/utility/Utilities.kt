package com.autohub.loginsignup.utility

import android.widget.ProgressBar

/**
 * Created by Vt Netzwelt
 */
class Utilities {
    companion object {
        fun animateProgressbar(progressbar: ProgressBar, from: Float, to: Float) {
            val anim = ProgressBarAnimation(progressbar, from, to)
            anim.duration = 1000
            progressbar.startAnimation(anim)
        }

    }


}

