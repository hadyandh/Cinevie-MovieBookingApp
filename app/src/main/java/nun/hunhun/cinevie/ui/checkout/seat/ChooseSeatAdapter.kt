package nun.hunhun.cinevie.ui.checkout.seat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import nun.hunhun.cinevie.R
import kotlin.collections.ArrayList

class ChooseSeatAdapter: RecyclerView.Adapter<ChooseSeatAdapter.ViewHolder>() {

    lateinit var context: Context
    private val TAG = "ChooseSeatAdapter"

    private val ABC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private val NUM = "123456789"
    private var mColumn = 5
    private var mRow = 5

    val mStateData = ArrayList<Int>()
    val mSeatData = ArrayList<Boolean>()
    val mSelectedList = ArrayList<Int>()

    private var mListener: OnSelectedChangedListener? = null
    fun setOnSelectedChangedListener(listener: OnSelectedChangedListener?) {
        mListener = listener
    }

    fun setRowAndColumn(column: Int, row: Int) {
        mColumn = column
        mRow = row
    }

    fun setData(data: List<Boolean>?) {
        mSeatData.clear()
        mStateData.clear()
        mSelectedList.clear()
        if (data != null) for (item in data) {
            mSeatData.add(item)
            if (item) {mStateData.add(-1)}
            else {mStateData.add(0)}
        }
    }

    private fun addToSelectedList(pos: Int) {
        mSelectedList.add(pos)
        mStateData[pos] = 1
        notifyItemChanged(pos)
        if (mListener != null) {mListener!!.onSelectedChanged(mSelectedList)}
    }

    private fun removeFromSelectedList(pos: Int) {
        mSelectedList.remove(pos)
        mStateData[pos] = 0
        notifyItemChanged(pos)
        if (mListener != null) {mListener!!.onSelectedChanged(mSelectedList)}
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_seat, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mStateData[position])
    }

    override fun getItemCount(): Int = mStateData.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        fun bind(state: Int) {
            val pos = adapterPosition
            val myRow: Int = pos / mRow
            val myColumn: Int = pos % mColumn
            if (itemView is TextView) {
                val s = "" + ABC.get(myRow) + NUM.get(myColumn)
                itemView.text = s
            }
            if (state == -1) {
                itemView.setBackgroundResource(R.drawable.bg_item_choose_seat_booked)
            } else if (state == 0) {
                itemView.setBackgroundResource(R.drawable.bg_item_choose_seat)
            } else {
                itemView.setBackgroundResource(R.drawable.bg_item_choose_seat_selected)
            }
        }

        override fun onClick(v: View) {
            val pos = adapterPosition
            if (mStateData.get(pos) == 0) {
                // mean that blank
                addToSelectedList(pos)
            } else if (mStateData.get(pos) == 1) {
                // mean that choosen
                removeFromSelectedList(pos)
            }
        }

        init {
            itemView.setOnClickListener(this)
        }
    }

    interface OnSelectedChangedListener {
        fun onSelectedChanged(selects: ArrayList<Int>)
    }
}
