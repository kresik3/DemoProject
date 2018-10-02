package com.krasovsky.dima.demoproject.main.view.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.krasovsky.dima.demoproject.base.view.fragment.ToolbarFragment
import com.krasovsky.dima.demoproject.main.R


class DeliveryFragment : ToolbarFragment() {

    override fun getTitle() = R.string.toolbar_delivery

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_delivery, container, false)
    }


}
