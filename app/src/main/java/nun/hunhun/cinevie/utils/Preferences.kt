package nun.hunhun.cinevie.utils

import android.content.Context
import android.content.SharedPreferences

class Preferences (val context: Context) {
    companion object{ const val USER_PREF = "user"}

    var sharedPreferences = context.getSharedPreferences(USER_PREF, 0)
    val editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun setValue(key: String, value: String){
        editor.putString(key, value)
        editor.apply()
    }

    fun checkValue(key: String) : Boolean {
        return sharedPreferences.contains(key)
    }

    fun getValue(key: String) : String? {
        return sharedPreferences.getString(key, "")
    }

    fun clearValue(){
        editor.clear()
        editor.apply()
    }
}