package nun.hunhun.cinevie.utils

import java.text.NumberFormat
import java.util.*

class ConvertBalance {

    fun rupiahFormat(balance: Double?) : String{
        val localeID = Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)
        return numberFormat.format(balance)
    }

    fun numberFormat(balance: Double?) : String{
        val numberFormat = NumberFormat.getCurrencyInstance()
        return numberFormat.format(balance)
    }

}