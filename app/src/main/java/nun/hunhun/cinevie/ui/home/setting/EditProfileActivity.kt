package nun.hunhun.cinevie.ui.home.setting

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_edit_profile.btn_back
import kotlinx.android.synthetic.main.activity_edit_profile.img_photo_user
import kotlinx.android.synthetic.main.activity_sign_up_photo.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import nun.hunhun.cinevie.R
import nun.hunhun.cinevie.model.User
import nun.hunhun.cinevie.utils.LoadingDialog
import nun.hunhun.cinevie.utils.Preferences


class EditProfileActivity : AppCompatActivity() {

    val REQUEST_IMAGE = 101
    private lateinit var filePath: Uri
    private lateinit var username: String

    private lateinit var preferences: Preferences
    private lateinit var loadingDialog: LoadingDialog

    private val reference = FirebaseDatabase.getInstance().getReference("User")
    private var storageReference: StorageReference = FirebaseStorage.getInstance().getReference("images/profile")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        initData()
    }

    private fun initData(){
        loadingDialog = LoadingDialog(this)
        preferences = Preferences(this) //for fragment
        username = preferences.getValue("username")!!

        getData()
    }

    private fun initView(user: User?) {
        val url = user?.photo
        edt_name.setText(user!!.name)
        edt_email.setText(user.email)
        edt_password.setText(user.password)

        if (!url.equals("")){
            Glide.with(this).load(url).into(img_photo_user)
        }

        btn_back.setOnClickListener { onBackPressed() }
        btn_photo.setOnClickListener { findPhoto() }
        btn_update.setOnClickListener { onUpdateData() }
    }

    private fun getData(){
        reference.child(username).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                initView(user)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@EditProfileActivity, ""+error.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun onUpdateData() {
        loadingDialog.start()

        val user = User()
        user.name = edt_name.text.toString()
        user.password = edt_password.text.toString()
        user.email = edt_email.text.toString()
        val userValue = user.toMap()

        reference.child(username).updateChildren(userValue)
            .addOnCompleteListener {
                Toast.makeText(this, "Your profile was updated", Toast.LENGTH_SHORT).show()
                loadingDialog.dismiss()
                finish()
            }
            .addOnFailureListener { Toast.makeText(this, "Can't update profile, please try again", Toast.LENGTH_SHORT).show() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK && data != null && data.data != null) {
            loadingDialog.start()
            filePath = data.data!!
            uploadPhoto()
        }
    }

    private fun uploadPhoto(){
        storageReference.child(username).putFile(filePath)
            .addOnSuccessListener {
                storageReference.child(username).downloadUrl.addOnSuccessListener {
                    reference.child(username).child("photo").setValue(it.toString())
                        .addOnSuccessListener {
                            loadingDialog.dismiss()
                            Glide.with(this).load(filePath).into(img_photo_user)
                            Toast.makeText(this, "Your profile picture was changed", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            loadingDialog.dismiss()
                            Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
                        }
                }
            }

            .addOnFailureListener {
                loadingDialog.dismiss()
                Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
            }

    }

    private fun findPhoto(){
        val pic = Intent()
        pic.type = "image/*"
        pic.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(pic, REQUEST_IMAGE)
    }
}