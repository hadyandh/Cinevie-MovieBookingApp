package nun.hunhun.cinevie.ui.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_onboarding_one.btn_register
import kotlinx.android.synthetic.main.activity_onboarding_one.btn_skip
import nun.hunhun.cinevie.R
import nun.hunhun.cinevie.ui.SignInActivity

class OnboardingTwoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_two)

        btn_register.setOnClickListener {
            startActivity(Intent(this, OnboardingThreeActivity::class.java))
        }

        btn_skip.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finishAffinity()
        }
    }
}