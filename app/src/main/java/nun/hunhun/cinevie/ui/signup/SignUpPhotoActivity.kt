package nun.hunhun.cinevie.ui.signup

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_sign_up_photo.*
import nun.hunhun.cinevie.ui.home.HomeActivity
import nun.hunhun.cinevie.R
import nun.hunhun.cinevie.utils.LoadingDialog
import nun.hunhun.cinevie.utils.Preferences

class SignUpPhotoActivity : AppCompatActivity() {

    val REQUEST_IMAGE = 101

    private lateinit var filePath: Uri
    private var reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("User")
    private var storageReference: StorageReference = FirebaseStorage.getInstance().getReference("images/profile")
    private lateinit var preferences: Preferences
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var username:String
    private lateinit var name:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_photo)

        name = intent.getStringExtra("name").toString()
        username = intent.getStringExtra("username").toString()
        btn_save.visibility = View.INVISIBLE

        preferences = Preferences(this)
        loadingDialog = LoadingDialog(this)

        tv_welcome_user.setText("Welcome,\n" + name)

        btn_back.setOnClickListener {
            onBackPressed()
        }

        btn_upload_later.setOnClickListener {
            finishAffinity()
            startActivity(Intent(this, HomeActivity::class.java))
        }

        btn_add_photo.setOnClickListener {
            findPhoto()
        }

        btn_save.setOnClickListener {

            loadingDialog.start()

            storageReference.child(username).putFile(filePath)
                .addOnSuccessListener {
                    storageReference.child(username).downloadUrl.addOnSuccessListener {
                        reference.child(username).child("photo").setValue(it.toString())
                            .addOnSuccessListener {
                                loadingDialog.dismiss()
                                finishAffinity()
                                startActivity(Intent(this, HomeActivity::class.java))
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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK && data != null && data.data != null) {
            filePath = data.data!!
            Glide.with(this).load(filePath).into(img_photo_user)
            btn_save.visibility = View.VISIBLE
        }
    }

    private fun findPhoto(){
        val pic = Intent()
        pic.type = "image/*"
        pic.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(pic, REQUEST_IMAGE)
    }
}