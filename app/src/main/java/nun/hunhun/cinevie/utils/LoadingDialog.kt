package nun.hunhun.cinevie.utils

import android.app.Activity
import android.app.AlertDialog
import nun.hunhun.cinevie.R

class LoadingDialog (val activity: Activity) {
    lateinit var dialog: AlertDialog

    fun start() {
        val builder = AlertDialog.Builder(activity)
        val view = activity.layoutInflater.inflate(R.layout.custom_dialog, null)
        builder.setView(view)
        dialog = builder.create()
        dialog.setCancelable(false)
        dialog.show()
    }

    fun dismiss() {
        dialog.dismiss()
    }
}
