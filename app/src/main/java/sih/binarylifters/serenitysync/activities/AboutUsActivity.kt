package sih.binarylifters.serenitysync.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import sih.binarylifters.serenitysync.R
import sih.binarylifters.serenitysync.constants.Constants
import sih.binarylifters.serenitysync.databinding.ActivityAboutUsBinding

class AboutUsActivity : AppCompatActivity() {

    private var binding: ActivityAboutUsBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutUsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setupToolbar()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding?.tbHome)
        supportActionBar?.title = "About Us"
        binding?.tbHome?.setTitleTextColor(resources.getColor(android.R.color.black, theme))
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}