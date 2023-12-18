package sih.binarylifters.serenitysync.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import sih.binarylifters.serenitysync.R
import sih.binarylifters.serenitysync.constants.Constants
import sih.binarylifters.serenitysync.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity(), View.OnClickListener {

    private var binding: ActivityHomeBinding? = null

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        auth = FirebaseAuth.getInstance()

        setupToolbar()
        setupOnClickListeners()
        if(Constants.IS_GUEST) {
            setupActivityForGuest()
        }
    }

    private fun setupActivityForGuest() {
        val cardViewAI = binding?.cvAi
        val cardViewMood = binding?.cvMood
        val cardViewRecords = binding?.cvRecords

        val disabledAlpha = 0.5f

        cardViewAI?.alpha = disabledAlpha
        cardViewMood?.alpha = disabledAlpha
        cardViewRecords?.alpha = disabledAlpha

        cardViewAI?.isClickable = false
        cardViewMood?.isClickable = false
        cardViewRecords?.isClickable = false

        val tintColor = ContextCompat.getColorStateList(this@HomeActivity, R.color.gray)
        cardViewAI?.backgroundTintList = tintColor
        cardViewMood?.backgroundTintList = tintColor
        cardViewRecords?.backgroundTintList = tintColor
    }


    private fun setupOnClickListeners() {
        binding?.cvTestYourself?.setOnClickListener(this@HomeActivity)
        binding?.cvAi?.setOnClickListener(this@HomeActivity)
        binding?.cvMood?.setOnClickListener(this@HomeActivity)
        binding?.cvRecords?.setOnClickListener(this@HomeActivity)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding?.tbHome)
        supportActionBar?.title = "Hello " + Constants.DISPLAY_NAME
        binding?.tbHome?.overflowIcon?.setTint(resources.getColor(android.R.color.black, theme))
        binding?.tbHome?.setTitleTextColor(resources.getColor(android.R.color.black, theme))
        val maxDisplayNameLength = 20

        val displayName = Constants.DISPLAY_NAME
        var truncatedDisplayName = if (displayName.length > maxDisplayNameLength) {
            displayName.substring(0, maxDisplayNameLength - 3) + "..."
        } else {
            displayName
        }

        if (displayName.isEmpty()) {
            truncatedDisplayName = "Guest"
        }

        supportActionBar?.title = "Welcome $truncatedDisplayName!"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.about -> {
                val intent = Intent(this@HomeActivity, AboutUsActivity::class.java)
                startActivity(intent)
            }
            R.id.help -> {

            }
            R.id.sign_out -> {
                if(Constants.IS_GUEST) {
                    val intent = Intent(this@HomeActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }else {
                    Constants.IS_GUEST = true
                    Constants.DISPLAY_NAME = ""
                    Constants.EMAIL_ID = ""
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build()

                    val googleSignInClient = GoogleSignIn.getClient(this, gso)

                    googleSignInClient.signOut().addOnCompleteListener { signOutTask ->
                        if (signOutTask.isSuccessful) {
                            FirebaseAuth.getInstance().signOut()
                            val intent = Intent(this@HomeActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                this@HomeActivity,
                                "Failed to sign out: ${signOutTask.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    val intent = Intent(this@HomeActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.cv_test_yourself -> {
                val intent = Intent(this@HomeActivity, TestingActivity::class.java)
                startActivity(intent)
            }
            R.id.cv_records -> {
                val intent = Intent(this@HomeActivity, RecordsActivity::class.java)
                startActivity(intent)
            }
            R.id.cv_ai -> {
                val intent =  Intent(this@HomeActivity, AIActivity::class.java)
                startActivity(intent)
            }
        }
    }
}