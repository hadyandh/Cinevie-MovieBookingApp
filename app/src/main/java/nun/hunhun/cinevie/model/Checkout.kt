package nun.hunhun.cinevie.model

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Checkout (
    var buyer: String ? = "",
    var title: String ? = "",
    var date: String ? = "",
    var total: Int ? = 0,
    var ticket: Int ? = 0,
    var seat: String ? = "",
    var poster: String ? = "",
    var key: String ? = ""
) : Parcelable {
    @Exclude
    fun toMapUserCheckout(): Map<String, Any?> {
        return mapOf(
            "title" to title,
            "date" to date,
            "total" to total,
            "ticket" to ticket,
            "seat" to seat,
            "poster" to poster
        )
    }

    @Exclude
    fun toMapTransaction(): Map<String, Any?> {
        return mapOf(
            "buyer" to buyer,
            "date" to date,
            "total" to total,
            "ticket" to ticket,
            "seat" to seat,
            "poster" to poster
        )
    }
}
