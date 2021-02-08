package nun.hunhun.cinevie.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import nun.hunhun.cinevie.R
import nun.hunhun.cinevie.ui.onboarding.OnboardingOneActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        var handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            startActivity(Intent(this, OnboardingOneActivity::class.java))
            finish()
        }, 2000)
    }
}
