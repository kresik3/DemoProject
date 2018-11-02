package com.krasovsky.dima.demoproject.base.dialog.zoom_viewer.container

import android.app.Dialog
import android.view.ViewGroup

import com.krasovsky.dima.demoproject.base.dialog.zoom_viewer.container.interfaces.TargetContainer


open class DialogContainer(private val mDialog: Dialog) : TargetContainer {

    override fun getDecorView(): ViewGroup? {
        return if (mDialog.window != null) mDialog.window!!.decorView as ViewGroup else null
    }
}
