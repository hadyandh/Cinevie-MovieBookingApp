package nun.hunhun.cinevie.ui.checkout.seat

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_choose_seat.*
import nun.hunhun.cinevie.R
import nun.hunhun.cinevie.model.Seat
import nun.hunhun.cinevie.model.Film
import nun.hunhun.cinevie.ui.checkout.CheckoutActivity
import nun.hunhun.cinevie.utils.GridItemDecoration
import java.util.*
import kotlin.collections.ArrayList

class ChooseSeatActivity : AppCompatActivity(), ChooseSeatAdapter.OnSelectedChangedListener {

    private var row = 5
    private var column = 5
    private var sumSeat = 25

    private var adapter: ChooseSeatAdapter = ChooseSeatAdapter()
    private lateinit var data: Film

    private lateinit var filmReference : DatabaseReference
    private lateinit var seatReference : DatabaseReference

    private var mSelects = ArrayList<Int>()
    private val statusSeatList = ArrayList<Boolean>()
    private val seatList = ArrayList<Seat>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_seat)

        data = intent.getParcelableExtra("data")!!
        filmReference = FirebaseDatabase.getInstance().getReference("Film").child(data.title.toString())
        seatReference = FirebaseDatabase.getInstance().getReference("Seat").child(data.title.toString())

        getData()
        initView()
    }

    private fun initView(){
        btn_back.setOnClickListener { onBackPressed() }
        btn_checkout.setOnClickListener { onCheckout() }
        setLayoutManager()
    }

    private fun setLayoutManager() {
        getSeatData(object : seatDataCallback {
            override fun onCallback(value: ArrayList<Boolean>) {
                adapter.setRowAndColumn(column, row)
                adapter.setOnSelectedChangedListener(this@ChooseSeatActivity)
                adapter.setData(value)

                rv_seat.addItemDecoration(GridItemDecoration(36, column))
                rv_seat.setAdapter(adapter)
                rv_seat.layoutManager = GridLayoutManager(this@ChooseSeatActivity, column)
            }
        })
    }

    //get data film from database realtime firebase
    private fun getData(){
        filmReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val film = snapshot.getValue(Film::class.java)
                tv_title_film.text = film?.title
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@ChooseSeatActivity,
                    "" + error.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun onCheckout(){
        val statusSeat = ArrayList<Boolean>()
        statusSeat.addAll(statusSeatList) //make duplicate arraylist from statusSeatList

        for (seat in seatList){
            statusSeat[seat.number!!] = true
        }

        data.statusSeat = statusSeat

        //intent
        val intent = Intent(this, CheckoutActivity::class.java)
        intent.putExtra("data", data)
        intent.putParcelableArrayListExtra("list", seatList) //put extra with arraylist
        startActivity(intent)
    }

    //change color button checkout when user click atleast one seat
    private fun stateCheckoutBtn(total: Int){
        if (total > 0){
            btn_checkout.isClickable = true
            btn_checkout.isEnabled = true
            btn_checkout.setTextColor(ContextCompat.getColor(this, R.color.white))
            btn_checkout.setText("CHECKOUT ("+total+")")
            btn_checkout.background = ContextCompat.getDrawable(this, R.drawable.shape_rounded_accent)
        } else {
            btn_checkout.isClickable = false
            btn_checkout.isEnabled = false
            btn_checkout.setTextColor(ContextCompat.getColor(this, R.color.darkerGrey))
            btn_checkout.setText("CHECKOUT")
            btn_checkout.background = ContextCompat.getDrawable(this, R.drawable.shape_rounded_blue_grey)
        }
    }

    override fun onSelectedChanged(selects: ArrayList<Int>) {
        mSelects.clear()
        seatList.clear()
        mSelects.addAll(selects)

        val ABC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val NUM = "123456789"

        for (select in selects) {
            var text = ""
            val seat = Seat()
            val myRow = select / row
            val myColumn = select % column
            text = String.format("%s%c%c ", text, ABC.get(myRow), NUM.get(myColumn))

            seat.number = select
            seat.seat = text
            seat.price = data.price
            seatList.add(seat)
        }
        stateCheckoutBtn(selects.size)
    }

    private fun getSeatData(callback: seatDataCallback){
        seatReference.child("status").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val status = snapshot.getValue(String::class.java)
                    val strs = status!!.split(", ")
                    strs.forEach { statusSeatList.add(it.toBoolean()) }
                } else {
                    repeat(sumSeat) { statusSeatList.add(false) }
                }
                callback.onCallback(statusSeatList)
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    interface seatDataCallback {
        fun onCallback(value: ArrayList<Boolean>)
    }
}