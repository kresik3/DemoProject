package com.krasovsky.dima.demoproject.main.list.recyclerview

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.krasovsky.dima.demoproject.base.util.picasso.PicassoUtil
import com.krasovsky.dima.demoproject.main.R
import com.krasovsky.dima.demoproject.main.command.action.fragment.ShowDishesByCategoryAction
import com.krasovsky.dima.demoproject.main.command.view.IActionCommand
import com.krasovsky.dima.demoproject.storage.model.MenuItemModel
import com.krasovsky.dima.demoproject.storage.retrofit.baseUrl


class MenuAdapter() : RecyclerView.Adapter<MenuAdapter.ViewHolder>() {

    var array: List<MenuItemModel>? = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.layout_menu_item, p0, false))
    }

    override fun getItemCount() = array?.size ?: 0

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bind(array!![p1])
    }

    open class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val image = itemView.findViewById<ImageView>(R.id.menu_item_image)
        private val label = itemView.findViewById<TextView>(R.id.menu_item_label)

        fun bind(data: MenuItemModel) {
            label.text = data.text
            PicassoUtil.setImagePicasso(baseUrl + data.iconPath, image)
            itemView.setOnClickListener {
                (itemView.context as AppCompatActivity as IActionCommand)
                        .sendCommand(ShowDishesByCategoryAction(data.id, data.text))
            }
        }

    }

}