package com.krasovsky.dima.demoproject.base.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.renderscript.*
import android.support.annotation.NonNull
import android.support.annotation.Nullable
import android.view.View


private const val MAX_RADIUS = 25f

class RSBlurProcessor(private val rs: RenderScript) {

    @Nullable
    fun blur(@NonNull bitmap: Bitmap, radius: Float, repeat: Int = 0): Bitmap? {
        val width = bitmap.width
        val height = bitmap.height

        // Create allocation type
        val bitmapType = Type.Builder(rs, Element.RGBA_8888(rs))
                .setX(width)
                .setY(height)
                .setMipmaps(false) // We are using MipmapControl.MIPMAP_NONE
                .create()

        // Create allocation
        val allocation = Allocation.createTyped(rs, bitmapType)

        // Create blur script
        val blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
        blurScript!!.setRadius(if (radius > MAX_RADIUS) MAX_RADIUS else radius)

        // Copy data to allocation
        allocation!!.copyFrom(bitmap)

        // set blur script input
        blurScript.setInput(allocation)

        // invoke the script to blur
        blurScript.forEach(allocation)

        // Repeat the blur for extra effect
        for (i in 0 until repeat) {
            blurScript.forEach(allocation)
        }

        // copy data back to the bitmap
        allocation.copyTo(bitmap)

        // release memory
        allocation.destroy()
        blurScript.destroy()

        return bitmap
    }

    fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val c = Canvas(bitmap)
        view.draw(c)
        return bitmap
    }

}