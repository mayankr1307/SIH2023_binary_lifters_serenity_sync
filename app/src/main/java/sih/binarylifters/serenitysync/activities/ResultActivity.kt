package sih.binarylifters.serenitysync.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import sih.binarylifters.serenitysync.R
import sih.binarylifters.serenitysync.constants.Constants
import sih.binarylifters.serenitysync.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    private var binding: ActivityResultBinding? = null
    private var testName = ""
    private var testScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val receivedIntent = intent
        testName = receivedIntent.getStringExtra("TEST_NAME").toString()
        testScore = receivedIntent.getIntExtra("SCORE", 0)

        setupToolbar()

        if(testName == "PHQ-9") {
            showResultForPHQ()
        }

        binding?.btnFinish?.setOnClickListener {
            finish()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showResultForPHQ() {
        val assessmentText: String
        val textColor: Int
        val score = testScore

        when (score) {
            in 0..4 -> {
                assessmentText = "Minimal depression"
                textColor = getColor(R.color.very_light_green)
            }
            in 5..9 -> {
                assessmentText = "Mild depression"
                textColor = getColor(R.color.darker_green)
            }
            in 10..14 -> {
                assessmentText = "Moderate depression"
                textColor = getColor(R.color.orange)
            }
            in 15..19 -> {
                assessmentText = "Moderately severe depression"
                textColor = getColor(R.color.red)
            }
            in 20..27 -> {
                assessmentText = "Severe depression"
                textColor = getColor(R.color.dark_red)
            }
            else -> {
                assessmentText = "Unable to assess"
                textColor = getColor(R.color.default_text_color)
            }
        }

        binding?.tvAssessmentResult?.setTextColor(textColor)
        binding?.tvAssessmentResult?.text = assessmentText
        binding?.tvScore?.text = "Score: $score"
        binding?.tvName?.text = "Hello ${Constants.DISPLAY_NAME}!"
    }

    private fun setupToolbar() {
        setSupportActionBar(binding?.tbResult)
        supportActionBar?.title = "Result $testName"
        binding?.tbResult?.setTitleTextColor(resources.getColor(android.R.color.black, theme))
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}