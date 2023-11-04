package sih.binarylifters.serenitysync.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import sih.binarylifters.serenitysync.adapters.TestsAdapter
import sih.binarylifters.serenitysync.constants.Constants
import sih.binarylifters.serenitysync.databinding.ActivityTestingBinding

class TestingActivity : AppCompatActivity() {

    private var binding: ActivityTestingBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestingBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setupToolbar()

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val recyclerView = binding?.rvTests // Reference to your RecyclerView in the layout
        val adapter = TestsAdapter(
            this,
            Constants.testNames
        ) // Replace 'yourTestDataList' with your actual data

        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(this)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding?.tbTest)
        supportActionBar?.title = "Assess yourself"
        binding?.tbTest?.setTitleTextColor(resources.getColor(android.R.color.black, theme))
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}