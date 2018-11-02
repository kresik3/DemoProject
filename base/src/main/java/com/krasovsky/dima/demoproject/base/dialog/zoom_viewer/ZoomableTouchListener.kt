package com.krasovsky.dima.demoproject.base.dialog.zoom_viewer

import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.ImageView
import android.widget.LinearLayout

import com.krasovsky.dima.demoproject.base.dialog.zoom_viewer.container.interfaces.TargetContainer
import com.krasovsky.dima.demoproject.base.util.picasso.PicassoUtil


internal class ZoomableTouchListener(private val mTargetContainer: TargetContainer,
                                     private val mTarget: View, private val url: String) : View.OnTouchListener {
    private var mZoomableView: ImageView? = null
    private var mShadow: View? = null
    private val mEndingZoomAction = Runnable {
        removeFromDecorView(mShadow)
        removeFromDecorView(mZoomableView)
        mTarget.visibility = View.VISIBLE
        mZoomableView = null

        showSystemUI()
    }

    override fun onTouch(v: View, ev: MotionEvent): Boolean {

        if (ev.pointerCount > 1) return true

        val action = ev.action and MotionEvent.ACTION_MASK

        when (action) {
            MotionEvent.ACTION_DOWN -> startViewZoomingView()
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> endZoomingView()
        }
        return true
    }

    private fun endZoomingView() {
        mEndingZoomAction.run()
    }

    private fun startViewZoomingView() {
        mZoomableView = ImageView(mTarget.context)
        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        layoutParams.setMargins(50, 100, 50, 100)
        mZoomableView!!.layoutParams = layoutParams
        PicassoUtil.setImagePicasso(url, mZoomableView!!)

        if (mShadow == null) {
            mShadow = View(mTarget.context).apply { setBackgroundResource(0) }
        }

        addToDecorView(mShadow!!)
        addToDecorView(mZoomableView!!)

        disableParentTouch(mTarget.parent)
        mTarget.visibility = View.INVISIBLE

        hideSystemUI()
    }


    private fun addToDecorView(v: View) {
        mTargetContainer.getDecorView()?.addView(v)
    }

    private fun removeFromDecorView(v: View?) {
        mTargetContainer.getDecorView()?.removeView(v)
    }

    private fun hideSystemUI() {
        mTargetContainer.getDecorView()?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    private fun showSystemUI() {
        mTargetContainer.getDecorView()?.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
    }

    private fun disableParentTouch(view: ViewParent) {
        view.requestDisallowInterceptTouchEvent(true)
        if (view.parent != null) disableParentTouch(view.parent)
    }
}
