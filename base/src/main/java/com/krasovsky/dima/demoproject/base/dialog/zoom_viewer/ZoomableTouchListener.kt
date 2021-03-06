package com.krasovsky.dima.demoproject.base.dialog.zoom_viewer

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Handler
import android.support.v7.widget.CardView
import android.view.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView

import android.widget.LinearLayout

import com.krasovsky.dima.demoproject.base.dialog.zoom_viewer.container.interfaces.TargetContainer
import com.krasovsky.dima.demoproject.base.util.picasso.PicassoUtil
import android.renderscript.RenderScript
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.krasovsky.dima.demoproject.base.util.RSBlurProcessor


internal class ZoomableTouchListener(private val mTargetContainer: TargetContainer,
                                     private val mTarget: View, private val url: String,
                                     private val animation: AnimationZomable) : View.OnTouchListener {

    class Size(val scaleX: Float, val scaleY: Float)
    class ColorForeground(val gray: Int, val transparent: Int)

    private val size = Size(0.8f, 0.8f)
    private val color = ColorForeground(Color.parseColor("#A3353535"), Color.parseColor("#00FFFFFF"))

    private val blurProcessor: RSBlurProcessor by lazy { RSBlurProcessor(RenderScript.create(mTarget.context)) }

    var listener: ((View) -> Unit)? = null

    private var mZoomableView: View? = null
    private var mShadow: View? = null

    private var isShown = false

    private val showHandler = Handler()
    private val showRunnable = Runnable {
        startViewZoomingView()
        animation.animateShowView(mZoomableView, mShadow)
    }
    private val hideRunnable = Runnable {
        removeFromDecorView(mShadow)
        removeFromDecorView(mZoomableView)
        mZoomableView = null
        mShadow = null
    }

    override fun onTouch(v: View, ev: MotionEvent): Boolean {
        if (ev.pointerCount > 1) return true
        if (!isShown) v.onTouchEvent(ev)

        val action = ev.action and MotionEvent.ACTION_MASK
        when (action) {
            MotionEvent.ACTION_DOWN -> actionDown()
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> actionUp(v, ev)
        }
        return true
    }

    private fun actionDown() {
        (mTarget as ImageView).setColorFilter(color.gray, PorterDuff.Mode.SRC_ATOP)
        showHandler.postDelayed(showRunnable, 300)
    }

    private fun actionUp(v: View, ev: MotionEvent) {
        (mTarget as ImageView).setColorFilter(color.transparent, PorterDuff.Mode.SRC_ATOP)
        endZoomingView()
        if (!isShown) {
            showHandler.removeCallbacks(showRunnable)
        }
    }

    private fun endZoomingView() {
        isShown = false
        animation.animateHideView(mZoomableView, mShadow, hideRunnable)
    }

    private fun startViewZoomingView() {
        isShown = true

        val imageView = getImageView()
        PicassoUtil.setImagePicasso(url, imageView)
        mZoomableView = getView(imageView)

        mShadow = ImageView(mTarget.context).apply {
            val v1 = (mTarget.context as AppCompatActivity).window.decorView.rootView
            setImageBitmap(blurProcessor.blur(blurProcessor.getBitmapFromView(v1)))
        }
        addToDecorView(mShadow!!)
        addToDecorView(mZoomableView!!)

        disableParentTouch(mTarget.parent)
    }

    private fun getView(imageView: ImageView): View {
        val screenSize = Util.getScreenSize(mTarget.context)
        return CardView(mTarget.context)
                .apply {
                    layoutParams = getLayoutParamsRoot()
                    addView(imageView)
                    x = (screenSize.x / 2f) - layoutParams.width / 2
                    y = (screenSize.y / 2f) - layoutParams.height / 2
                }
    }

    private fun getImageView(): ImageView {
        return ImageView(mTarget.context)
                .apply { layoutParams = getLayoutParamsImage() }
    }

    private fun getLayoutParamsImage(): LinearLayout.LayoutParams {
        return LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
    }

    private fun getLayoutParamsRoot(): LinearLayout.LayoutParams {
        val screenSize = Util.getScreenSize(mTarget.context)
        val width = (screenSize.x * size.scaleX).toInt()
        val height = (screenSize.y * size.scaleY).toInt()
        val size = Math.min(width, height)
        return LinearLayout.LayoutParams(size, size)
    }

    private fun addToDecorView(v: View) {
        mTargetContainer.getDecorView()?.addView(v)
    }

    private fun removeFromDecorView(v: View?) {
        mTargetContainer.getDecorView()?.removeView(v)
    }

    private fun disableParentTouch(view: ViewParent) {
        view.requestDisallowInterceptTouchEvent(true)
        if (view.parent != null) disableParentTouch(view.parent)
    }
}
