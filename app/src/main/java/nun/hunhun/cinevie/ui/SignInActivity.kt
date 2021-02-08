package nun.hunhun.cinevie.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_sign_in.*
import nun.hunhun.cinevie.R
import nun.hunhun.cinevie.model.User
import nun.hunhun.cinevie.ui.home.HomeActivity
import nun.hunhun.cinevie.ui.signup.SignUpActivity
import nun.hunhun.cinevie.utils.Preferences

class SignInActivity : AppCompatActivity() {

    private lateinit var username: String
    private lateinit var password: String

    private lateinit var preferences: Preferences

    private var reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("User")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        preferences = Preferences(this)

        /**
         * Check key username in shared preferences
         */
        if (preferences.checkValue("username") == true){
            startActivity(Intent(this, HomeActivity::class.java))
            finishAffinity()
        }

        btn_new_account.setOnClickListener {startActivity(Intent(this, SignUpActivity::class.java))}
        btn_login.setOnClickListener {
            username = edt_username.text.toString()
            password = edt_password.text.toString()

            if (TextUtils.isEmpty(username)){
                edt_username.error = "Please input your username"
                edt_username.requestFocus()
            } else if (TextUtils.isEmpty(password)){
                edt_password.error = "Please input your password"
                edt_password.requestFocus()
            } else{
                pushLogin(username, password)
            }
        }
    }

    private fun pushLogin(username: String, password: String) {
        reference.child(username).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var user = snapshot.getValue(User::class.java)

                if (user != null){
                    if (user.password.equals(password)){
                        preferences.setValue("username", user.username.toString())
                        startActivity(Intent(this@SignInActivity, HomeActivity::class.java))
                        finishAffinity()
                    } else{
                        Toast.makeText(this@SignInActivity, "Sorry, Your password is wrong. Please try again", Toast.LENGTH_LONG).show()
                    }
                } else{
                    Toast.makeText(this@SignInActivity, "Username not found", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SignInActivity, error.message, Toast.LENGTH_LONG).show()
            }
        })
    }

}