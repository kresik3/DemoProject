package com.krasovsky.dima.demoproject.main.util

import android.content.Context
import android.view.View
import com.krasovsky.dima.demoproject.base.map.ContentViewMapper
import com.krasovsky.dima.demoproject.base.model.ObjectTypeContent
import com.krasovsky.dima.demoproject.storage.model.InfoObject

class Mapper {

    companion object {
        fun mapInfoObjectArray(context: Context, array: MutableList<InfoObject>): MutableList<View> {
            val result = mutableListOf<View>()
            array.forEach {
                result.add(ContentViewMapper.mapInfoObjectArray(context, ObjectTypeContent(it.type, it.content)))
            }
            return result
        }
    }
}