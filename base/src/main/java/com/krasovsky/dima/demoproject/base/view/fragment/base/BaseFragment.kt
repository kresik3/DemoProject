package com.krasovsky.dima.demoproject.base.view.fragment.base

import android.support.v4.app.Fragment
import com.krasovsky.dima.demoproject.base.controller.dialog.DialogManager

abstract class BaseFragment : Fragment() {

    private val dialogManager: DialogManager by lazy { DialogManager() }

    open fun showProgressDialog() {
        dialogManager.showProgressDialog(context!!)
    }

    open fun hideProgressDialog() {
        dialogManager.hideProgressDialog()
    }

    override fun onDestroy() {
        super.onDestroy()
        dialogManager.hideDialogs()
    }
}