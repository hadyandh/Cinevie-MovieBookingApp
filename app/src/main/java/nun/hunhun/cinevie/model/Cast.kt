package nun.hunhun.cinevie.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Cast (
    var nama: String ? = "",
    var char: String ? = "",
    var url: String ? = ""
) : Parcelable
