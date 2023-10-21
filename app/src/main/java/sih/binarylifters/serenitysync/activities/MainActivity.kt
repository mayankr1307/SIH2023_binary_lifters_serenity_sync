package sih.binarylifters.serenitysync.activities

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import sih.binarylifters.serenitysync.R
import sih.binarylifters.serenitysync.constants.Constants
import sih.binarylifters.serenitysync.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this@MainActivity, gso)

        binding?.flSignInGoogle?.setOnClickListener {
            signInGoogle()
        }
        binding?.flSignInGuest?.setOnClickListener {
            signInGuest()
        }
    }

    private fun signInGuest() {
        val dialog = Dialog(this@MainActivity)
        dialog.setContentView(R.layout.dialog_guest_sign_in)
        dialog.setCancelable(false)

        val btnNo = dialog.findViewById<TextView>(R.id.tv_no)
        val btnYes = dialog.findViewById<TextView>(R.id.tv_yes)

        btnNo.setOnClickListener {
            dialog.dismiss()
        }

        btnYes.setOnClickListener {
            val intent = Intent(this@MainActivity, HomeActivity::class.java)
            Constants.EMAIL_ID = ""
            Constants.DISPLAY_NAME = ""
            Constants.IS_GUEST = true
            dialog.dismiss()
            startActivity(intent)
            finish()
        }

        dialog.show()
    }

    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result ->
            if(result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResults(task)
            }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if(task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if(account != null) {
                updateUI(account)
            }
        }else {
            Toast.makeText(
                this@MainActivity,
                task.exception.toString(),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful) {
                val intent = Intent(this@MainActivity, HomeActivity::class.java)
                Constants.EMAIL_ID = account.email.toString()
                Constants.DISPLAY_NAME = account.displayName.toString()
                Constants.IS_GUEST = false
                startActivity(intent)
                finish()
            }else {
                Toast.makeText(
                    this@MainActivity,
                    it.exception.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}