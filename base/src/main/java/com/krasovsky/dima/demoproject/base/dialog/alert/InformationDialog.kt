package com.krasovsky.dima.demoproject.base.dialog.alert

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView
import com.krasovsky.dima.demoproject.base.R
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.textColor
import kotlin.properties.Delegates

class InformationDialog : DialogFragment() {

    class Builder {

        private var layoutDialog: View by Delegates.notNull()
        private val informationDialog: InformationDialog by lazy { InformationDialog() }

        fun initView(context: Context, base: Boolean = false) {
            layoutDialog = context.layoutInflater.inflate(R.layout.dialog_information_layout, null)
            val buttonNegotive = layoutDialog.findViewById<TextView>(R.id.button_negotive_dialog)
            buttonNegotive.visibility = View.INVISIBLE
            initBaseColors(context)
        }

        private fun initBaseColors(context: Context) {
            layoutDialog.findViewById<TextView>(R.id.title_dialog).textColor = ContextCompat.getColor(context, R.color.darkTextColor)
            layoutDialog.findViewById<TextView>(R.id.message_dialog).textColor = ContextCompat.getColor(context, R.color.darkTextColor)
            layoutDialog.findViewById<TextView>(R.id.button_negotive_dialog).textColor = ContextCompat.getColor(context, R.color.accentColor)
            layoutDialog.findViewById<TextView>(R.id.button_positive_dialog).textColor = ContextCompat.getColor(context, R.color.accentColor)
        }

        fun setTitle(title: String) {
            layoutDialog.findViewById<TextView>(R.id.title_dialog).text = title
        }

        fun setMessage(message: String?) {
            layoutDialog.findViewById<TextView>(R.id.message_dialog).text = message
        }

        fun setPositiveBtn(text: String, action: (() -> Unit)? = null) {
            val local = Resources.getSystem().configuration.locale
            layoutDialog.findViewById<TextView>(R.id.button_positive_dialog)
                    .apply {
                        visibility = View.VISIBLE
                        this.text = text.toUpperCase(local)
                        setOnClickListener {
                            action?.invoke()
                            informationDialog.dialog.cancel()
                        }
                    }
        }

        fun setNegativeBtn(text: String, action: (() -> Unit)? = null) {
            val local = Resources.getSystem().configuration.locale
            layoutDialog.findViewById<TextView>(R.id.button_negotive_dialog)
                    .apply {
                        visibility = View.VISIBLE
                        this.text = text.toUpperCase(local)
                        setOnClickListener {
                            action?.invoke()
                            informationDialog.dialog.cancel()
                        }
                    }
        }

        fun build(): InformationDialog = informationDialog.apply { initDialog(layoutDialog) }
    }

    private var viewDialog: View by Delegates.notNull()

    private fun initDialog(view: View) {
        viewDialog = view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        this.isCancelable = false

        val builder = AlertDialog.Builder(activity)
                .setView(viewDialog)
        return builder.create()
    }

}