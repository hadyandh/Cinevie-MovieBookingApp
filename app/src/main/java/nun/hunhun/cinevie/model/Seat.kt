package nun.hunhun.cinevie.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Seat (
    var number: Int ? = 0,
    var seat: String ? = "",
    var price: Int ? = 0
) : Parcelable