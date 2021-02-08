package nun.hunhun.cinevie.ui.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import nun.hunhun.cinevie.R
import nun.hunhun.cinevie.model.User
import nun.hunhun.cinevie.utils.Preferences

class SignUpActivity : AppCompatActivity() {

    private lateinit var name: String
    private lateinit var username: String
    private lateinit var email: String
    private lateinit var password: String

    private lateinit var preferences: Preferences
    private var reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("User")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        btn_register.setOnClickListener {

            name = edt_name.text.toString()
            username = edt_username.text.toString()
            email = edt_email.text.toString()
            password = edt_password.text.toString()

            if (TextUtils.isEmpty(name)) {
                edt_name.error = "Please enter your name"
                edt_name.requestFocus()
            } else if (TextUtils.isEmpty(username)) {
                edt_username.error = "Please enter your username"
                edt_username.requestFocus()
            } else if (TextUtils.isEmpty(password)) {
                edt_password.error = "Please enter your password"
                edt_password.requestFocus()
            } else if (password.length < 6) {
                edt_password.error = "Your password must be at least 6 character"
                edt_password.requestFocus()
            } else if (TextUtils.isEmpty(email)) {
                edt_email.error = "Please enter your email"
                edt_email.requestFocus()
            } else {
                val user = User()
                user.username = username
                user.name = name
                user.password = password
                user.email = email

                checkUsername(username, user)
            }
        }

        btn_back.setOnClickListener { finish() }
    }

    private fun checkUsername(username: String, user: User) {
        reference.child(username).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(
                        this@SignUpActivity,
                        "Username already taken, please choose another username",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    insertData(username, user)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SignUpActivity, "" + error.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun insertData(username: String, user: User) {
        reference.child(username).setValue(user)
            .addOnSuccessListener {
                preferences = Preferences(this)
                preferences.setValue("username", username)

                val intent = Intent(this, SignUpPhotoActivity::class.java)
                intent.putExtra("name", user.name)
                intent.putExtra("username", user.username)
                startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(
                    this@SignUpActivity,
                    "Failure input data, please try again or check your connection",
                    Toast.LENGTH_LONG
                ).show()
            }
    }
}