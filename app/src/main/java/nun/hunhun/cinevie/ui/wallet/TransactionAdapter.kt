package nun.hunhun.cinevie.ui.wallet

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import nun.hunhun.cinevie.R
import nun.hunhun.cinevie.model.Wallet
import nun.hunhun.cinevie.utils.ConvertBalance

class TransactionAdapter(private var data: List<Wallet>) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(data[position], context)
    }

    override fun getItemCount(): Int = data.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val convert = ConvertBalance()
        private val tvName: TextView = view.findViewById(R.id.tv_name_transaction)
        private val tvDate: TextView = view.findViewById(R.id.tv_date_transaction)
        private val tvAmount: TextView = view.findViewById(R.id.tv_amount_transaction)

        fun bindItem(data:Wallet, context: Context){
            val amountConvert = convert.rupiahFormat(data.amount?.toDouble())

            tvName.text = data.title
            tvDate.text = data.date

            if (data.topup!!){
                tvAmount.text = "+ " + amountConvert
                tvAmount.setTextColor(ContextCompat.getColor(context, R.color.colorGreen))
            } else {
                tvAmount.text = "- " + amountConvert
                tvAmount.setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
            }
        }
    }

}
