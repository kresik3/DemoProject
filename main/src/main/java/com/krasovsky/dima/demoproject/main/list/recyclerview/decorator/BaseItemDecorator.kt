package com.krasovsky.dima.demoproject.main.list.recyclerview.decorator

import android.graphics.Rect
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import com.krasovsky.dima.demoproject.main.R

class BaseItemDecorator(val spaceDimenId: Int = R.dimen.base_space_2x) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                state: RecyclerView.State) {
        if(parent.getChildAdapterPosition(view) == 0) {
            outRect.top = view.context.resources.getDimensionPixelOffset(spaceDimenId)
        }
    }
}