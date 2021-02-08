package nun.hunhun.cinevie.ui.home.setting

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.fragment_setting.*
import nun.hunhun.cinevie.R
import nun.hunhun.cinevie.model.User
import nun.hunhun.cinevie.ui.SignInActivity
import nun.hunhun.cinevie.ui.wallet.MyWalletActivity
import nun.hunhun.cinevie.utils.Preferences


class SettingFragment : Fragment() {

    private lateinit var preferences: Preferences
    private val reference = FirebaseDatabase.getInstance().getReference("User")

    private lateinit var username: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
        initView()
    }

    private fun initData(){
        preferences = Preferences(activity!!.applicationContext) //for fragment
        username = preferences.getValue("username")!!

        getData()
    }

    private fun initView(){
        menu_wallet.setOnClickListener { startActivity(
            Intent(
                context,
                MyWalletActivity::class.java
            )
        ) }
        menu_edit_profile.setOnClickListener { startActivity(
            Intent(
                context,
                EditProfileActivity::class.java
            )
        ) }
        menu_sign_out.setOnClickListener { onSignOut() }
    }

    private fun getData(){
        reference.child(username).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)!!
                val photo = user.photo
                tv_name_setting.text = user.name
                tv_username_setting.text = "@" + user.username

                if (!photo.equals("")) {
                    Glide.with(this@SettingFragment).load(user.photo).into(iv_photo_user_setting)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "" + error.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun onSignOut(){
        preferences.clearValue()
        startActivity(Intent(context, SignInActivity::class.java))
        activity!!.finishAffinity()
    }
}