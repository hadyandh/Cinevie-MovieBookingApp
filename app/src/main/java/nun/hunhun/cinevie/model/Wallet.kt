package nun.hunhun.cinevie.model

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Wallet (
    var amount: Int ? = 0,
    var title: String ? = "",
    var date: String ? = "",
    var topup: Boolean ? = false
) : Parcelable{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "amount" to amount,
            "date" to date,
            "title" to title,
            "topup" to topup
        )
    }
}