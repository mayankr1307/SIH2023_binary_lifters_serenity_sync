package sih.binarylifters.serenitysync.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import sih.binarylifters.serenitysync.R
import sih.binarylifters.serenitysync.classes.AssessmentData
import sih.binarylifters.serenitysync.databinding.ActivityAiBinding

class AIActivity : AppCompatActivity() {

    private var binding: ActivityAiBinding? = null
    private lateinit var assessmentList: ArrayList<AssessmentData>
    private lateinit var user: FirebaseUser
    private var prompt = "Below are the test details, including scores and dates for 5 tests. Could you provide an evaluation or analysis of these tests? Keep the response to the point."
    private var tests = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        assessmentList = ArrayList()
        user = FirebaseAuth.getInstance().currentUser!!

        setupToolbar()
        retrieveLastFiveAssessments(user.uid)

        binding?.ivAiAnalysis?.setOnClickListener {

        }
    }

    private fun retrieveLastFiveAssessments(userId: String) {
        val userAssessmentRef = FirebaseDatabase.getInstance().getReference("users").child(userId)
            .child("assessments")
            .orderByChild("date")
            .limitToLast(5)

        userAssessmentRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                assessmentList.clear()
                for (assessmentSnapshot in snapshot.children) {
                    val assessment = assessmentSnapshot.getValue(AssessmentData::class.java)
                    assessment?.let {
                        assessmentList.add(it)
                    }
                }

                for(test in assessmentList) {
                    tests += "${test.testName} Score: ${test.score} Date: ${test.date}\n"
                }
                prompt +=  '\n' + tests
                Log.e("Prompt", prompt)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@AIActivity,
                    "$error",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun setupToolbar() {
        setSupportActionBar(binding?.tbAi)
        supportActionBar?.title = "AI Analysis"
        binding?.tbAi?.setTitleTextColor(resources.getColor(android.R.color.black, theme))
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}