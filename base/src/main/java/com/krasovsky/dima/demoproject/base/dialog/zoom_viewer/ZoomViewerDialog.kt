package com.krasovsky.dima.demoproject.base.dialog.zoom_viewer

import android.app.Activity
import android.app.Dialog
import android.app.DialogFragment
import android.view.View

import com.krasovsky.dima.demoproject.base.dialog.zoom_viewer.container.ActivityContainer
import com.krasovsky.dima.demoproject.base.dialog.zoom_viewer.container.DialogContainer
import com.krasovsky.dima.demoproject.base.dialog.zoom_viewer.container.DialogFragmentContainer
import com.krasovsky.dima.demoproject.base.dialog.zoom_viewer.container.interfaces.TargetContainer
import kotlin.properties.Delegates

class ZoomViewerDialog private constructor(private var mTargetContainer: TargetContainer) {

    private val animation = AnimationZomable()

    class Builder {
        private var mTargetContainer: TargetContainer

        constructor(activity: Activity) {
            mTargetContainer = ActivityContainer(activity)
        }

        constructor(dialog: Dialog) {
            mTargetContainer = DialogContainer(dialog)
        }

        constructor(dialogFragment: DialogFragment) {
            mTargetContainer = DialogFragmentContainer(dialogFragment)
        }

        fun build() = ZoomViewerDialog(mTargetContainer)
    }

    fun register(target: View, url: String, listener: ((View) -> Unit)? = null) {
        target.setOnTouchListener(ZoomableTouchListener(mTargetContainer, target,
                url, animation).apply {
            this.listener = listener
        })
    }

    fun unregister(view: View) {
        view.setOnTouchListener(null)
    }
}
