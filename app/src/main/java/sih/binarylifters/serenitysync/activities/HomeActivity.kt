package sih.binarylifters.serenitysync.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
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
            R.id.about_us -> {
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
                    auth.signOut()
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
        }
    }
}