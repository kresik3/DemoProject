package com.krasovsky.dima.demoproject.base.view.activity.base

import android.support.v7.app.AppCompatActivity
import com.krasovsky.dima.demoproject.base.controller.dialog.DialogManager

abstract class BaseActivity : AppCompatActivity() {

    private val dialogManager: DialogManager by lazy { DialogManager() }

    protected fun showProgressDialog() {
        dialogManager.showProgressDialog(this)
    }

    protected fun hideProgressDialog() {
        dialogManager.hideProgressDialog()
    }

    override fun onDestroy() {
        super.onDestroy()
        dialogManager.hideDialogs()
    }
}