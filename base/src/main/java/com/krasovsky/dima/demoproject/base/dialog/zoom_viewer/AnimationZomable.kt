package com.krasovsky.dima.demoproject.base.dialog.zoom_viewer

import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

class AnimationZomable {

    class StartState(val alpha: Float, val scaleX: Float, val scaleY: Float)

    private val startState = StartState(0f, 0.7f, 0.7f)
    private val activeState = StartState(1f, 1f, 1f)

    private val interpolator = AccelerateDecelerateInterpolator()
    private val duration: Long = 200L

    fun animateHideView(view: View?, shadowView: View?, hideRunnable: Runnable) {
        if (view == null) hideRunnable.run()
        view?.animate()
                ?.alpha(startState.alpha)
                ?.scaleX(startState.scaleX)
                ?.scaleY(startState.scaleY)
                ?.withStartAction { animateHideBG(shadowView) }
                ?.setInterpolator(interpolator)
                ?.setDuration(duration)
                ?.withEndAction(hideRunnable)?.start()
    }

    private fun animateHideBG(shadowView: View?) {
        shadowView?.animate()
                ?.alpha(startState.alpha)
                ?.setInterpolator(interpolator)
                ?.setDuration(duration)?.start()
    }

    fun animateShowView(view: View?, shadowView: View?) {
        applyStartState(view, shadowView)
        view?.animate()
                ?.alpha(activeState.alpha)
                ?.scaleX(activeState.scaleX)
                ?.scaleY(activeState.scaleY)
                ?.withStartAction { animateShowBG(shadowView) }
                ?.setInterpolator(interpolator)
                ?.setDuration(duration)?.start()
    }

    private fun animateShowBG(shadowView: View?) {
        shadowView?.animate()
                ?.alpha(activeState.alpha)
                ?.setInterpolator(interpolator)
                ?.setDuration(duration)?.start()
    }

    private fun applyStartState(view: View?, shadowView: View?) {
        view?.apply {
            alpha = startState.alpha
            scaleX = startState.scaleX
            scaleY = startState.scaleY
        }

        shadowView?.apply {
            alpha = startState.alpha
        }
    }

}