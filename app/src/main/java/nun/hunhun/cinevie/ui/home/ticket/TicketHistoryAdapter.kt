package nun.hunhun.cinevie.ui.home.ticket

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import nun.hunhun.cinevie.R
import nun.hunhun.cinevie.model.Checkout
import nun.hunhun.cinevie.model.Film

class TicketHistoryAdapter(private var data: List<Checkout>, private val listener: (Checkout) -> Unit) : RecyclerView.Adapter<TicketHistoryAdapter.ViewHolder>() {

    lateinit var context: Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        context = parent.context
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_ticket_history, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(data[position], listener, context)
    }

    override fun getItemCount(): Int = data.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvTitle: TextView = view.findViewById(R.id.tv_title)
        private val tvDate: TextView = view.findViewById(R.id.tv_date)
        private val tvTicket: TextView = view.findViewById(R.id.tv_amount_ticket)
        private val ivPoster: ImageView = view.findViewById(R.id.iv_poster_film)

        fun bindItem(data:Checkout, listener: (Checkout) -> Unit, context: Context){
            tvTitle.setText(data.title)
            tvDate.setText(data.date)
            tvTicket.setText("${data.ticket} Ticket")

            Glide.with(context).load(data.poster).into(ivPoster)

            itemView.setOnClickListener {
                listener(data)
            }
        }
    }

}
