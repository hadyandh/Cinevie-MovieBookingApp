package nun.hunhun.cinevie.ui.home.ticket

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_ticket.*
import nun.hunhun.cinevie.R
import nun.hunhun.cinevie.model.Checkout
import nun.hunhun.cinevie.utils.Preferences

class TicketFragment : Fragment() {

    private var reference =  FirebaseDatabase.getInstance().getReference("history_checkout")
    private lateinit var preferences: Preferences

    private var checkoutList = ArrayList<Checkout>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ticket, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        init()
    }

    private fun init(){
        preferences = Preferences(activity!!.applicationContext) //for fragment
        val username = preferences.getValue("username")!!

        rv_ticket_history.layoutManager = LinearLayoutManager(context)
        getData(username)
    }

    private fun getData(username: String){
        reference.child(username).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                checkoutList.clear()
                for (data in snapshot.children){
                    val key = data.key
                    val checkout = data.getValue(Checkout::class.java)

                    if (checkout != null){
                        checkoutList.add(checkout)
                        checkout.key = key
                    }

                    rv_ticket_history.adapter = TicketHistoryAdapter(checkoutList){
                        startActivity(Intent(context, TicketDetailActivity::class.java).putExtra("key", it.key))
                    }
                }
                tv_amount_ticket.text = checkoutList.size.toString() + " Movies"
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, ""+error.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}