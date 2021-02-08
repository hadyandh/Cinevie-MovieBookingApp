package nun.hunhun.cinevie.ui.home.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_sign_up_photo.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import nun.hunhun.cinevie.ui.detail.FilmDetailActivity
import nun.hunhun.cinevie.R
import nun.hunhun.cinevie.model.Film
import nun.hunhun.cinevie.model.User
import nun.hunhun.cinevie.utils.ConvertBalance
import nun.hunhun.cinevie.utils.Preferences
import java.util.*

class DashboardFragment : Fragment() {

    private lateinit var preferences: Preferences
    private val convert = ConvertBalance()

    private var reference = FirebaseDatabase.getInstance().getReference()

    private var comingSoonList = ArrayList<Film>()
    private var nowPlayingList = ArrayList<Film>()
    private lateinit var username: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_now_playing.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rv_coming_soon.layoutManager = LinearLayoutManager(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        /* using this for Activity
        preferences = Preferences(this)
         */
        preferences = Preferences(activity!!.applicationContext) //for fragment
        username = preferences.getValue("username")!!

        getDataFirebase()
    }

    private fun getDataFirebase() {
        /** get data user */
        reference.child("User").child(username).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)!!
                val photoUrl = user.photo
                val balance = user.balance?.toDouble()

                tv_name.text = user.name
                tv_balance.text = convert.rupiahFormat(balance)

                if (!photoUrl.equals("")) {
                    Glide.with(this@DashboardFragment).load(photoUrl).into(iv_photo_user)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "" + error.message, Toast.LENGTH_LONG).show()
            }
        })

        /** get data film */
        reference.child("now_playing").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                nowPlayingList.clear()
                for (filmSnapshot in snapshot.children) {
                    val film = filmSnapshot.getValue(Film::class.java)

                    if (film != null) {
                        nowPlayingList.add(film)
                    }

                    rv_now_playing.adapter = NowPlayingAdapter(nowPlayingList) {
                        startActivity(
                            Intent(
                                context,
                                FilmDetailActivity::class.java
                            ).putExtra("data", it)
                        )
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "" + error.message, Toast.LENGTH_LONG).show()
            }
        })

        reference.child("coming_soon").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                comingSoonList.clear()
                for (filmSnapshot in snapshot.children) {
                    val film = filmSnapshot.getValue(Film::class.java)

                    if (film != null) {
                        comingSoonList.add(film)
                    }

                    rv_coming_soon.adapter = ComingSoonAdapter(comingSoonList) {
                        startActivity(
                            Intent(
                                context,
                                FilmDetailActivity::class.java
                            ).putExtra("data", it)
                        )
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "" + error.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}