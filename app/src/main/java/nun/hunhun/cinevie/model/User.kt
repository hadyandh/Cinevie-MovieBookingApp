package nun.hunhun.cinevie.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var name: String? = "",
    var password: String? = "",
    var email: String? = "",
    var username: String? = "",
    var photo: String? = "",
    var balance: Int? = 0
){
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "email" to email,
            "password" to password,
        )
    }
}