package com.krasovsky.dima.demoproject.main.command.action.badge.model

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.krasovsky.dima.demoproject.main.R
import java.lang.ref.WeakReference

class BudgeModel(val context: WeakReference<Context>, val baseBackground: Drawable, val circleBackground: Drawable) {

    private lateinit var budgeContainer: ViewGroup

    fun applyBudgeToItem(container: ViewGroup) {
        val view = container.findViewById<ViewGroup>(R.id.root_badge_view)
        if (view != null) {
            budgeContainer = view
        } else {
            budgeContainer = LayoutInflater.from(context.get()).inflate(R.layout.layout_budge, container, false) as ViewGroup
            container.addView(budgeContainer)
        }
    }

    fun getBadgeCounterView(): TextView {
        return budgeContainer.findViewById(R.id.budge_count)
    }
}

