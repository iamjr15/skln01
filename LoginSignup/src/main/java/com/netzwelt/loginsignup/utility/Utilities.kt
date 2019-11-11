package com.netzwelt.loginsignup.utility

import android.widget.ProgressBar


class Utilities {
    companion object {
        fun animateProgressbar(progressbar: ProgressBar, from: Float, to: Float) {
            val anim = ProgressBarAnimation(progressbar, from, to)
            anim.duration = 1000
            progressbar.startAnimation(anim)
        }

    }


}

