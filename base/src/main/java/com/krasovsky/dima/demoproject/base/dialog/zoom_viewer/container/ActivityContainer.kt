package com.krasovsky.dima.demoproject.base.dialog.zoom_viewer.container

import android.app.Activity
import android.view.ViewGroup

import com.krasovsky.dima.demoproject.base.dialog.zoom_viewer.container.interfaces.TargetContainer


class ActivityContainer(private val mActivity: Activity) : TargetContainer {

    override fun getDecorView(): ViewGroup {
        return mActivity.window.decorView as ViewGroup
    }
}
