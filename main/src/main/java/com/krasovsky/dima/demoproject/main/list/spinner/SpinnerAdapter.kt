package com.krasovsky.dima.demoproject.main.list.spinner

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.krasovsky.dima.demoproject.main.R
import org.jetbrains.anko.layoutInflater


class SpinnerAdapter(context: Context, textViewResourceId: Int,
                     private val objects: List<String?>) : ArrayAdapter<String>(context, textViewResourceId, objects) {

    override fun getDropDownView(position: Int, convertView: View?,
                        parent: ViewGroup?): View? {
        return getCustomDropDownView(position, convertView, parent)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        return getCustomView(position, convertView, parent)
    }

    fun getCustomView(position: Int, convertView: View?,
                      parent: ViewGroup?): View? {
        val inflater = context.layoutInflater
        val row = inflater.inflate(R.layout.spinner_item, parent, false)
        (row.findViewById(R.id.name) as TextView).text = objects[position]
        return row
    }

    fun getCustomDropDownView(position: Int, convertView: View?,
                      parent: ViewGroup?): View? {
        val inflater = context.layoutInflater
        val row = inflater.inflate(R.layout.spinner_dropdown_item, parent, false)
        (row.findViewById(R.id.name_dropdown) as TextView).text = objects[position]
        return row
    }
}