package nun.hunhun.cinevie.ui.checkout

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import nun.hunhun.cinevie.R
import nun.hunhun.cinevie.model.Seat
import nun.hunhun.cinevie.utils.ConvertBalance

class CheckoutSeatAdapter(private var data: List<Seat>, private val inflate: Int) : RecyclerView.Adapter<CheckoutSeatAdapter.ViewHolder>() {

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(LayoutInflater.from(parent.context).inflate(inflate, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(data[position])
    }

    override fun getItemCount(): Int = data.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val convert = ConvertBalance()
        private val tvSeat: TextView = view.findViewById(R.id.tv_seat)
        private val tvPrice: TextView = view.findViewById(R.id.tv_price)

        fun bindItem(data:Seat){
            tvSeat.text = "Seat "+data.seat
            tvPrice.text = convert.rupiahFormat(data.price!!.toDouble())
        }
    }

}
