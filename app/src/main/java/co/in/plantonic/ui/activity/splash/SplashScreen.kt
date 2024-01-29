package co.`in`.plantonic.ui.activity.splash


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import co.`in`.plantonic.databinding.ActivitySplashScreenBinding
import co.`in`.plantonic.ui.activity.home.HomeActivity
import co.`in`.plantonic.ui.activity.logInSignUp.login.LoginActivity
import co.`in`.plantonic.ui.activity.splash.SplashScreenViewModel
import co.`in`.plantonic.utils.StorageUtil.Companion.getInstance
import com.google.firebase.auth.FirebaseAuth

class SplashScreen : AppCompatActivity() {
    private lateinit var splashScreenViewModel: SplashScreenViewModel
    private lateinit var binding : ActivitySplashScreenBinding
    private val localStorage = getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize storage
        localStorage.sharedPref =
            this.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)

        // Initialize viewModel
        splashScreenViewModel = ViewModelProvider(this).get(
            SplashScreenViewModel::class.java
        )
        binding.motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(motionLayout: MotionLayout, startId: Int, endId: Int) {
            }

            override fun onTransitionChange(motionLayout: MotionLayout, startId: Int, endId: Int, progress: Float) {
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
                checkUser()
            }

            override fun onTransitionTrigger(motionLayout: MotionLayout, triggerId: Int, positive: Boolean, progress: Float) {
            }
        })
    }

    private fun checkUser() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            splashScreenViewModel.checkIfUserExists(FirebaseAuth.getInstance().uid).observe(this, Observer { userExists ->
                // if user exists in firebase
                if (userExists != null && userExists) {

                    // check if user exists in server and get jwt token
                    FirebaseAuth.getInstance().currentUser?.uid?.let {
                        splashScreenViewModel.getUserToken(it).observe(this@SplashScreen) { token ->

                            if (token != null) {
                                Log.d("yyuvujtyfvui", token)
                                // save token to local storage
                                localStorage.token = token

                                startActivity(Intent(this@SplashScreen, HomeActivity::class.java))
                                finish()
                            }else{
                                startActivity(Intent(this@SplashScreen, LoginActivity::class.java))
                                finish()
                            }
                        }
                    }
                } else {
                    startActivity(Intent(this@SplashScreen, LoginActivity::class.java))
                    finish()
                }
            })
        } else {
            startActivity(Intent(this@SplashScreen, LoginActivity::class.java))
            finish()
        }
    }
}