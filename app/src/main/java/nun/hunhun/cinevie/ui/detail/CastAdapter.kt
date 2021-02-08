package nun.hunhun.cinevie.ui.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import nun.hunhun.cinevie.R
import nun.hunhun.cinevie.model.Cast

class CastAdapter(private var data: List<Cast>) : RecyclerView.Adapter<CastAdapter.ViewHolder>() {

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_cast_film, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(data[position], context)
    }

    override fun getItemCount(): Int = data.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvName: TextView = view.findViewById(R.id.tv_name_cast)
        private val tvChar: TextView = view.findViewById(R.id.tv_char_cast)
        private val ivPhoto: ImageView = view.findViewById(R.id.img_photo_cast)

        fun bindItem(data:Cast, context: Context){
            tvName.setText(data.nama)
            tvChar.setText(data.char)

            Glide.with(context).load(data.url).into(ivPhoto)
        }
    }

}
