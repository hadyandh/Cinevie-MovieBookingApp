package nun.hunhun.cinevie.ui.checkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_checkout_success.*
import nun.hunhun.cinevie.R
import nun.hunhun.cinevie.model.Film
import nun.hunhun.cinevie.model.Seat
import nun.hunhun.cinevie.ui.home.ticket.TicketDetailActivity
import nun.hunhun.cinevie.ui.home.HomeActivity

class CheckoutSuccessActivity : AppCompatActivity() {

    private var seatList = ArrayList<Seat>()
    private lateinit var data: Film
    private lateinit var key: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout_success)

        initData()
        initView()
    }

    private fun initData(){
        key = intent.extras?.getString("key")!!
    }

    private fun initView(){
        btn_home.setOnClickListener {intentHome()}

        btn_ticket.setOnClickListener {
            val intent = Intent(this, TicketDetailActivity::class.java)
            intent.putExtra("key", key)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        intentHome()
    }

    private fun intentHome(){
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

}