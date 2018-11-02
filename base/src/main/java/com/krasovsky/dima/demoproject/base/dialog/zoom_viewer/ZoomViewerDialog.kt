package com.krasovsky.dima.demoproject.base.dialog.zoom_viewer

import android.app.Activity
import android.app.Dialog
import android.app.DialogFragment
import android.view.View

import com.krasovsky.dima.demoproject.base.dialog.zoom_viewer.container.ActivityContainer
import com.krasovsky.dima.demoproject.base.dialog.zoom_viewer.container.DialogContainer
import com.krasovsky.dima.demoproject.base.dialog.zoom_viewer.container.DialogFragmentContainer
import com.krasovsky.dima.demoproject.base.dialog.zoom_viewer.container.interfaces.TargetContainer

object ZoomViewerDialog {

    class Builder {

        private var mTargetContainer: TargetContainer? = null
        private var mTargetView: View? = null

        constructor(activity: Activity) {
            this.mTargetContainer = ActivityContainer(activity)
        }

        constructor(dialog: Dialog) {
            this.mTargetContainer = DialogContainer(dialog)
        }

        constructor(dialogFragment: DialogFragment) {
            this.mTargetContainer = DialogFragmentContainer(dialogFragment)
        }

        fun target(target: View): Builder {
            this.mTargetView = target
            return this
        }

        fun register(url: String) {
            mTargetView?.setOnTouchListener(ZoomableTouchListener(mTargetContainer!!, mTargetView!!, url))
        }
    }

    fun unregister(view: View) {
        view.setOnTouchListener(null)
    }
}
