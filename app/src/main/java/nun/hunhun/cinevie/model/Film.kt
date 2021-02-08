package nun.hunhun.cinevie.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Film (
    var desc: String ? = "",
    var director: String ? = "",
    var title: String ? = "",
    var genre: String ? = "",
    var poster: String ? = "",
    var price: Int ? = 0,
    var rating: String ? = "",
    var statusSeat: ArrayList<Boolean> = ArrayList()
) : Parcelable
