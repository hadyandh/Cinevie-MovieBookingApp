package nun.hunhun.cinevie.ui.onboarding

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_onboarding_one.*
import nun.hunhun.cinevie.R
import nun.hunhun.cinevie.ui.SignInActivity

class OnboardingOneActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_one)

        sharedPreferences = getSharedPreferences("onboarding", 0)

        val check = sharedPreferences.getString("not_first", "")
        if (check.equals("1")){
            startActivity(Intent(this, SignInActivity::class.java))
            finishAffinity()
        }

        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("not_first", "1")
        editor.apply()

        btn_register.setOnClickListener {
            startActivity(Intent(this, OnboardingTwoActivity::class.java))
        }

        btn_skip.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finishAffinity()
        }
    }
}