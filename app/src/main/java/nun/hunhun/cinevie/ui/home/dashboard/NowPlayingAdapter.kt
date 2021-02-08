package nun.hunhun.cinevie.ui.home.dashboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import nun.hunhun.cinevie.R
import nun.hunhun.cinevie.model.Film

class NowPlayingAdapter(private var data: List<Film>, private val listener: (Film) -> Unit) : RecyclerView.Adapter<NowPlayingAdapter.ViewHolder>() {

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_now_playing, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(data[position], listener, context)
    }

    override fun getItemCount(): Int = data.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvTitle: TextView = view.findViewById(R.id.tv_title_film)
        private val tvGenre: TextView = view.findViewById(R.id.tv_genre_film)
        private val tvRating: TextView = view.findViewById(R.id.tv_rating_film)
        private val ivPoster: ImageView = view.findViewById(R.id.iv_poster_film)

        fun bindItem(data:Film, listener: (Film) -> Unit, context: Context){
            tvTitle.setText(data.title)
            tvGenre.setText(data.genre)
            tvRating.setText(data.rating)

            Glide.with(context).load(data.poster).into(ivPoster)

            itemView.setOnClickListener {
                listener(data)
            }
        }
    }

}
