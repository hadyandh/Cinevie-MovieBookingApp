package nun.hunhun.cinevie.ui.wallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_choose_seat.*
import kotlinx.android.synthetic.main.activity_top_up.*
import nun.hunhun.cinevie.R
import nun.hunhun.cinevie.model.Wallet
import nun.hunhun.cinevie.utils.ConvertBalance
import nun.hunhun.cinevie.utils.Preferences
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class TopUpActivity : AppCompatActivity() {

    private lateinit var preferences: Preferences
    private val reference = FirebaseDatabase.getInstance().getReference()

    private lateinit var username: String
    private var balance = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_up)

        initData()
        initView()
    }

    private fun initData(){
        preferences = Preferences(this)
        username = preferences.getValue("username")!!
        getData()
    }

    private fun initView() {
        btn_back_topup.setOnClickListener { onBackPressed() }
        btn_topup.setOnClickListener { onTopUp() }

        menu_10k.setOnClickListener { onClickQuickOption(menu_10k, 10000) }
        menu_25k.setOnClickListener { onClickQuickOption(menu_25k, 25000) }
        menu_50k.setOnClickListener { onClickQuickOption(menu_50k, 50000) }
        menu_100k.setOnClickListener { onClickQuickOption(menu_100k, 100000) }
        menu_500k.setOnClickListener { onClickQuickOption(menu_500k, 500000) }
        menu_750k.setOnClickListener { onClickQuickOption(menu_750k, 750000) }

        edt_amount.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                if (!edt_amount.text.toString().equals("")){
                    onStateButton()
                }
            }
        })
    }

    private fun getData(){
        val userListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                balance = snapshot.child("balance").getValue(Int::class.java)!!
                val convertBalance = ConvertBalance()
                val balanceFormat = convertBalance.rupiahFormat(balance.toDouble())

                tv_balance_topup.text = balanceFormat
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@TopUpActivity, ""+error.message, Toast.LENGTH_SHORT).show()
            }
        }
        reference.child("User").child(username).addValueEventListener(userListener)
    }

    private fun onTopUp(){
        val key = reference.child("Wallet").child(username).child("history").push().key
        if (key == null) {
            return
        }

        val amount = edt_amount.text.toString().toInt()
        val newBalance = balance + amount

        val wallet = Wallet(amount, "Top Up", getCurrentDate(), true).toMap()
        val walletValue = hashMapOf<String, Any>(
            "/Wallet/$username/history/$key" to wallet,
            "/Wallet/$username/balance" to newBalance,
            "User/$username/balance" to newBalance
        )

        reference.updateChildren(walletValue)
            .addOnCompleteListener { finish() }
            .addOnFailureListener { Toast.makeText(this, "Something error, please try again", Toast.LENGTH_SHORT).show() }
    }

    private fun onClickQuickOption(textView: TextView, amount: Int){
        deselectMenu()
        seletMenu(textView)

        edt_amount.setText(amount.toString())
    }

    private fun deselectMenu(){
        menu_10k.setBackgroundResource(R.drawable.stroke_line_grey_rounded_16dp)
        menu_25k.setBackgroundResource(R.drawable.stroke_line_grey_rounded_16dp)
        menu_50k.setBackgroundResource(R.drawable.stroke_line_grey_rounded_16dp)
        menu_100k.setBackgroundResource(R.drawable.stroke_line_grey_rounded_16dp)
        menu_500k.setBackgroundResource(R.drawable.stroke_line_grey_rounded_16dp)
        menu_750k.setBackgroundResource(R.drawable.stroke_line_grey_rounded_16dp)
    }

    private fun seletMenu(textView: TextView){
        textView.setBackgroundResource(R.drawable.shape_rounded_accent_16dp)
    }

    private fun onStateButton(){
        val amount = edt_amount.text.toString().toInt()

        if (amount != 10000 && amount != 25000 && amount != 50000 && amount != 100000 && amount != 500000 && amount != 750000){ deselectMenu() }
        if (amount >= 10000){
            btn_topup.isClickable = true
            btn_topup.isEnabled = true
            btn_topup.setTextColor(ContextCompat.getColor(this@TopUpActivity, R.color.white))
            btn_topup.background = ContextCompat.getDrawable(this@TopUpActivity, R.drawable.shape_rounded_accent)
        } else {
            btn_topup.isClickable = false
            btn_topup.isEnabled = false
            btn_topup.setTextColor(ContextCompat.getColor(this@TopUpActivity, R.color.darkerGrey))
            btn_topup.background = ContextCompat.getDrawable(this@TopUpActivity, R.drawable.shape_rounded_blue_grey)
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