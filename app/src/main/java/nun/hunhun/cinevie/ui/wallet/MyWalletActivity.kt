package nun.hunhun.cinevie.ui.wallet

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.activity_my_wallet.*
import nun.hunhun.cinevie.R
import nun.hunhun.cinevie.model.Wallet
import nun.hunhun.cinevie.utils.ConvertBalance
import nun.hunhun.cinevie.utils.Preferences

class MyWalletActivity : AppCompatActivity() {

    private lateinit var preferences: Preferences
    private lateinit var convertBalance: ConvertBalance
    private val userReference = FirebaseDatabase.getInstance().getReference("User")
    private val walletReference = FirebaseDatabase.getInstance().getReference("Wallet")

    private lateinit var username: String
    private val list = ArrayList<Wallet>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_wallet)

        initData()
        initView()
    }

    private fun initData(){
        preferences = Preferences(this)
        convertBalance = ConvertBalance()
        username = preferences.getValue("username")!!

        getData()
    }

    private fun initView(){
        btn_back_wallet.setOnClickListener { onBackPressed() }
        btn_goto_topup.setOnClickListener { startActivity(Intent(this, TopUpActivity::class.java)) }

        /** set layout manager */
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.reverseLayout = true /** set reverse order for recyclerview */
        linearLayoutManager.stackFromEnd = true
        rv_transaction.layoutManager = linearLayoutManager
    }

    private fun getData(){
        val userListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val balance = snapshot.child("balance").getValue(Double::class.java)
                val convert = convertBalance.rupiahFormat(balance)
                tv_wallet_balance.text = convert
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MyWalletActivity, "" + error.message, Toast.LENGTH_SHORT).show()
            }
        }

        val walletListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (walletSnapshot in snapshot.children){
                        val wallet = walletSnapshot.getValue(Wallet::class.java)!!
                        list.add(wallet)
                    }
                    rv_transaction.adapter = TransactionAdapter(list)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MyWalletActivity, "" + error.message, Toast.LENGTH_SHORT).show()
            }
        }

        userReference.child(username).addValueEventListener(userListener)
        walletReference.child(username).child("history").addValueEventListener(walletListener)
    }
}