package nun.hunhun.cinevie.ui.home.ticket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.activity_detail_film.*
import kotlinx.android.synthetic.main.activity_ticket_detail.*
import nun.hunhun.cinevie.R
import nun.hunhun.cinevie.model.Checkout
import nun.hunhun.cinevie.model.Seat
import nun.hunhun.cinevie.ui.checkout.CheckoutSeatAdapter
import nun.hunhun.cinevie.utils.Preferences

class TicketDetailActivity : AppCompatActivity() {

    private lateinit var preferences : Preferences
    private var checkoutReference =  FirebaseDatabase.getInstance().getReference("history_checkout")
    private var filmReference =  FirebaseDatabase.getInstance().getReference("Film")

    private var seatList = ArrayList<Seat>()
    private lateinit var key: String
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_detail)

        initData()
        btn_back_ticket_detail.setOnClickListener { onBackPressed() }
    }

    private fun initData(){
        preferences = Preferences(this)
        username = preferences.getValue("username")!!
        key = intent.getStringExtra("key")!!

        getDataCheckout()
    }

    private fun setLayoutManager(){
        rv_checkout_ticket.layoutManager = LinearLayoutManager(this)
        rv_checkout_ticket.adapter = CheckoutSeatAdapter(seatList, R.layout.item_seat_ticket_detail)
    }

    private fun getDataCheckout(){
        checkoutReference.child(username).child(key).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val checkout = snapshot.getValue(Checkout::class.java)!!
                tv_date_ticket.text = checkout.date

                stringToList(checkout.seat)
                setLayoutManager()
                getDataFilm(checkout.title!!)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun getDataFilm(title : String){
        filmReference.child(title).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val poster = snapshot.child("poster").getValue(String::class.java)
                tv_title_ticket.text = title
                tv_genre_ticket.text = snapshot.child("genre").getValue(String::class.java)
                tv_rating_ticket.text = snapshot.child("rating").getValue(String::class.java)
                Glide.with(this@TicketDetailActivity).load(poster).into(iv_poster_ticket)

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@TicketDetailActivity, ""+error.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun stringToList(strs : String?){
        val item = strs!!.split(" , ")
        item.forEach {
            val seat = Seat()
            seat.seat = it
            seatList.add(seat)
        }
    }
}