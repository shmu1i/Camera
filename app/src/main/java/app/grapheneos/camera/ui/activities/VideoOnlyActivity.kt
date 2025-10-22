package app.grapheneos.camera.ui.activities

import android.os.Bundle
import android.widget.Toast
import app.grapheneos.camera.R
import app.grapheneos.camera.util.isVideoDisabled

class VideoOnlyActivity : MainActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if video recording is disabled via kt.novideo property
        if (isVideoDisabled()) {
            Toast.makeText(this, "Video recording is disabled on this device", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        captureButton.setImageResource(R.drawable.recording)

        tabLayout.alpha = 0f
        tabLayout.isClickable = false
        tabLayout.isEnabled = false
//        (tabLayout.layoutParams as ViewGroup.MarginLayoutParams).let {
//            it.setMargins(it.leftMargin, it.height, it.rightMargin, it.bottomMargin)
//            it.height = 0
//        }
//
//        (previewView.layoutParams as ViewGroup.MarginLayoutParams).let {
//            it.setMargins(it.leftMargin, it.topMargin, it.rightMargin, 0)
//        }
    }

}