package com.krasovsky.dima.demoproject.base.view.fragment.base

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.krasovsky.dima.demoproject.main.view.controller.dialog.DialogManager

abstract class BaseFragment : Fragment() {

    private val dialogManager: DialogManager by lazy { DialogManager() }

    fun showProgressDialog() {
        dialogManager.showProgressDialog(context!!)
    }

    fun hideProgressDialog() {
        dialogManager.hideProgressDialog()
    }

    override fun onDestroy() {
        super.onDestroy()
        dialogManager.hideDialogs()
    }
}