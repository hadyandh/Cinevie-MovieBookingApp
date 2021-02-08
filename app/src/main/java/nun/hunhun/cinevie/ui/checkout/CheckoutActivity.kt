package nun.hunhun.cinevie.ui.checkout

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_checkout.*
import nun.hunhun.cinevie.R
import nun.hunhun.cinevie.model.Checkout
import nun.hunhun.cinevie.model.Film
import nun.hunhun.cinevie.model.Seat
import nun.hunhun.cinevie.model.Wallet
import nun.hunhun.cinevie.utils.ConvertBalance
import nun.hunhun.cinevie.utils.LoadingDialog
import nun.hunhun.cinevie.utils.Preferences
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CheckoutActivity : AppCompatActivity(), DataCallback {

    private val convert = ConvertBalance()
    private lateinit var preferences: Preferences
    private lateinit var data: Film
    private val loadingDialog = LoadingDialog(this)

    private var reference =  FirebaseDatabase.getInstance().getReference()

    private var seatList = ArrayList<Seat>()
    private var totalPrice = 0
    private lateinit var seat: String
    private lateinit var username: String
    private var currentBalance = 0
    private var balanceAfterPay = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        initData()
    }

    /** use this callback to waiting firebase to get value.
     * everything in this callback will be execute after
     * firebase is done for getting value */
    override fun onCallback(balance: Int?) {
        currentBalance = balance!!
        initView()
    }

    private fun initData(){
        preferences = Preferences(this) //get data user from class utils.Preferences

        seatList = intent.getParcelableArrayListExtra("list")!!
        data = intent.getParcelableExtra("data")!!

        username = preferences.getValue("username")!!
        totalPrice = data.price!! * seatList.size
        seat = seatList.joinToString{it.seat.toString() } /**convert seat from seatList to string */
        onGetBalance()
    }

    private fun initView(){
        setLayoutManager()
        onCheckBalance()
        tv_date.text = getCurrentDate()
        tv_total_pay.text = convert.rupiahFormat(totalPrice.toDouble())
        btn_back.setOnClickListener { onBackPressed() }
        btn_cancel.setOnClickListener { onBackPressed() }
        btn_pay.setOnClickListener { onCheckout() }
    }

    private fun setLayoutManager(){
        rv_checkout.layoutManager = LinearLayoutManager(this)
        rv_checkout.adapter = CheckoutSeatAdapter(seatList, R.layout.item_checkout)
    }

    private fun onCheckout(){
        loadingDialog.start()

        val key = reference.child("transaction").child(data.title!!).push().key
        if (key == null) {
            Log.w("TAG", "Couldn't get push key for posts")
            return
        }

        val date = getCurrentDate()
        val ticket = seatList.size
        val wallet = Wallet(totalPrice, data.title, date).toMap()
        val checkout = Checkout(username, data.title, date, totalPrice, ticket, seat, data.poster)
        val checkoutUser = checkout.toMapUserCheckout()
        val checkoutFilm = checkout.toMapTransaction()

        val value = hashMapOf<String, Any>(
            "/Transaction/${data.title}/$key" to checkoutFilm,
            "/history_checkout/$username/$key" to checkoutUser,
            "/Seat/${data.title}/status" to data.statusSeat.joinToString(), /** convert list to string */
            "/Wallet/$username/history/$key" to wallet,
            "/Wallet/$username/balance" to balanceAfterPay,
            "User/$username/balance" to balanceAfterPay
        )

        reference.updateChildren(value)
            .addOnCompleteListener {
                loadingDialog.dismiss()

                val intent = Intent(this, CheckoutSuccessActivity::class.java)
                intent.putExtra("key", key)
                startActivity(intent)
                finishAffinity()
            }
            .addOnFailureListener {
                Toast.makeText(this, ""+it.message, Toast.LENGTH_LONG).show()
            }
    }

    private fun onGetBalance(){
        reference.child("User").child(username).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val balance = snapshot.child("balance").getValue(Int::class.java)

                /** to execute method onCallback(Line 51) */
                this@CheckoutActivity.onCallback(balance)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@CheckoutActivity, "" + error.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun onCheckBalance() {
        tv_total_balance.text = convert.rupiahFormat(currentBalance.toDouble())
        balanceAfterPay = currentBalance - totalPrice
        if (currentBalance < totalPrice) {
            tv_total_balance.setTextColor(
                ContextCompat.getColor(
                    this@CheckoutActivity,
                    R.color.colorAccent
                )
            )
            tv_alert.visibility = View.VISIBLE
            btn_pay.visibility = View.INVISIBLE
        }
    }

    private fun getCurrentDate(): String {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Makassar"))
        val date = Date()
        val localeID = Locale("in", "ID")
        val dateFormat = SimpleDateFormat("EEEE, d MMMM yyyy", localeID)
        return dateFormat.format(date)
    }
}

interface DataCallback {
    fun onCallback(balance: Int?)
}