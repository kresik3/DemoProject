package com.krasovsky.dima.demoproject.main.view.controller.dialog

import android.content.Context
import com.krasovsky.dima.demoproject.base.dialog.progress.ProgressDialog
import com.krasovsky.dima.demoproject.base.dialog.progress.asDialog

class DialogManager {

    private var progressDialog: ProgressDialog<Context>? = null

    fun showProgressDialog(context: Context) {
        if (progressDialog == null) {
            progressDialog = context.asDialog()
        }
        progressDialog?.showProgressDialog()
    }

    fun hideProgressDialog() {
        progressDialog?.hideProgressDialog()
    }

    fun hideDialogs() {
        progressDialog?.hideProgressDialog()
    }

}