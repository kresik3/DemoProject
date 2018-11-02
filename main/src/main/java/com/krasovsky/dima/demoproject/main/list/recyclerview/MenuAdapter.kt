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
import com.krasovsky.dima.demoproject.main.list.recyclerview.holder.EmptyVH
import com.krasovsky.dima.demoproject.storage.model.MenuItemModel


class MenuAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var titleEmpty = R.string.empty_list_title

    var array: List<MenuItemModel>? = listOf()
        set(value) {
            if (field?.size == 0 && value?.size != 0) {
                notifyItemRemoved(0)
            }
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): RecyclerView.ViewHolder {
        return when (type) {
            0 -> EmptyVH(LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_list_empty, parent, false))
            else -> ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_menu_item, parent, false))
        }
    }

    override fun getItemCount(): Int {
        val size = array?.size ?: 0
        return if (size == 0) 1 else size
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder) {
            is EmptyVH -> viewHolder.bind(titleEmpty)
            is ViewHolder -> viewHolder.bind(array!![position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return array?.size ?: 0
    }

    open class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val image = itemView.findViewById<ImageView>(R.id.menu_item_image)
        private val label = itemView.findViewById<TextView>(R.id.menu_item_label)

        fun bind(data: MenuItemModel) {
            label.text = data.text
            PicassoUtil.setImagePicasso(data.iconPath, image)
            itemView.setOnClickListener {
                (itemView.context as AppCompatActivity as IActionCommand)
                        .sendCommand(ShowDishesByCategoryAction(data.id, data.text))
            }
        }

    }

}