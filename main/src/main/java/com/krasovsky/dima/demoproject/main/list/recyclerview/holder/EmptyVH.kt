package com.krasovsky.dima.demoproject.main.list.recyclerview.holder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.krasovsky.dima.demoproject.main.R

class EmptyVH(view: View) : RecyclerView.ViewHolder(view) {
    private val tvTitle = itemView.findViewById<TextView>(R.id.title)

    fun bind(titleId: Int) {
        tvTitle.setText(titleId)
    }
}