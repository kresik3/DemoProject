package com.krasovsky.dima.demoproject.main.view.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.krasovsky.dima.demoproject.base.view.fragment.BackToolbarFragment
import com.krasovsky.dima.demoproject.base.view.fragment.base.BaseMenuFragment

import com.krasovsky.dima.demoproject.main.R
import com.krasovsky.dima.demoproject.main.view.activity.interfaces.IToolbarCommand
import android.support.v7.app.AppCompatActivity
import com.krasovsky.dima.demoproject.main.view.activity.interfaces.COMMAND_BACK


private const val KEY_CATEGORY_ID = "KEY_CATEGORY_ID"
private const val KEY_CATEGORY_NAME = "KEY_CATEGORY_NAME"

class DishesFragment : BackToolbarFragment() {

    override fun getTitle() = arguments?.getString(KEY_CATEGORY_NAME)?: ""

    companion object {
        fun getInstance(categoryID: String, categoryName: String): BaseMenuFragment {
            return DishesFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_CATEGORY_ID, categoryID)
                    putString(KEY_CATEGORY_NAME, categoryName)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dishes, container, false)
    }

    override fun onClickBack() {
        (context as AppCompatActivity as IToolbarCommand).sendCommand(COMMAND_BACK)
    }

}
