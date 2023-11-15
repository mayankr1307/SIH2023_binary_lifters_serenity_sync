package sih.binarylifters.serenitysync.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import sih.binarylifters.serenitysync.R
import sih.binarylifters.serenitysync.databinding.ActivityRecordsBinding

class RecordsActivity : AppCompatActivity() {

    private var binding: ActivityRecordsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecordsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setupToolbar()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding?.tbRecords)
        supportActionBar?.title = "Records"
        binding?.tbRecords?.setTitleTextColor(resources.getColor(android.R.color.black, theme))
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}