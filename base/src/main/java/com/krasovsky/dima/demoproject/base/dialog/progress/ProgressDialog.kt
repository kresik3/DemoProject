package com.krasovsky.dima.demoproject.base.dialog.progress

import android.content.Context
import android.app.ProgressDialog
import com.krasovsky.dima.demoproject.base.R
import java.lang.ref.WeakReference
import org.jetbrains.anko.indeterminateProgressDialog


class ProgressDialog<out T : Context> internal constructor(obj: T) {

    private val weakRef = WeakReference(obj)
    private var dialog: ProgressDialog? = null

    fun showProgressDialog(color: Int? = null) {
        if (weakRef.get() == null || dialog?.isShowing == true) return

        val context = weakRef.get() as Context
        if (dialog == null) {
            dialog = context.indeterminateProgressDialog(context.getString(R.string.loading))
            dialog?.setCancelable(false)
        }

        if (color != null) {
            val lProgressBar = android.widget.ProgressBar(context, null, android.R.attr.progressBarStyle)
            lProgressBar.indeterminateDrawable.setColorFilter(color, android.graphics.PorterDuff.Mode.MULTIPLY)
            dialog?.setIndeterminateDrawable(lProgressBar.indeterminateDrawable)
        }

        dialog?.show()
    }

    fun hideProgressDialog() {
        dialog?.dismiss()
    }

}

fun <T : Context> T.asDialog() = com.krasovsky.dima.demoproject.base.dialog.progress.ProgressDialog(this)