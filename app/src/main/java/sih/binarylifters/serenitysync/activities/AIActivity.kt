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
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import sih.binarylifters.serenitysync.classes.AssessmentData
import sih.binarylifters.serenitysync.databinding.ActivityAiBinding
import java.io.IOException

class AIActivity : AppCompatActivity() {

    private var binding: ActivityAiBinding? = null
    private lateinit var assessmentList: ArrayList<AssessmentData>
    private lateinit var user: FirebaseUser
    private var prompt = "Below are the test details, including scores and dates for 5 tests. Could you provide an evaluation or analysis of these tests? Keep the response to the point."
    private var tests = ""
    private var resultsFetched = false
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        assessmentList = ArrayList()
        user = FirebaseAuth.getInstance().currentUser!!

        setupToolbar()
        retrieveLastFiveAssessments(user.uid)

        binding?.ivAiAnalysis?.setOnClickListener {
            if(!resultsFetched) {
                Toast.makeText(
                    this@AIActivity,
                    "Please wait while the results are being fetched...",
                    Toast.LENGTH_SHORT
                ).show()
            }
            getResponse(prompt){response ->
                runOnUiThread {
                    binding?.tvResponse?.text = response
                }
            }
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

                resultsFetched = true
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

    private fun getResponse(question: String, callback: (String) -> Unit) {
        val apiKey = "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"
        val url = "https://api.openai.com/v1/completions"

        val requestBody = """
            {
            "model": "gpt-3.5-turbo-instruct",
            "prompt": "Say this is a test",
            "max_tokens": 7,
            "temperature": 500
            }
        """.trimIndent()

        val request = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer $apiKey")
            .post(requestBody.toRequestBody("application/json".toMediaTypeOrNull()))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("error", "API failed", e)
                binding?.tvResponse?.text = e.message
                callback(e.message.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                if (body != null) {
                    Log.v("data", body)
                } else {
                    Log.v("data", "empty")
                    callback("Empty response body")
                }
//                val jsonObject = JSONObject(body)
//                val jsonArray: JSONArray = jsonObject.getJSONArray("choices")
//                val textResult = jsonArray.getJSONObject(0).getString("text")
//                callback(textResult)
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