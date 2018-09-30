package com.krasovsky.dima.demoproject.main.util

import android.content.Context
import android.view.View
import com.krasovsky.dima.demoproject.base.map.ContentViewMapper
import com.krasovsky.dima.demoproject.base.map.InflaytingModel
import com.krasovsky.dima.demoproject.base.model.ObjectTypeContent
import com.krasovsky.dima.demoproject.storage.model.InfoObject
import java.util.zip.Inflater

class Mapper {

    companion object {
        fun mapInfoObjectArray(inflater: InflaytingModel, array: MutableList<InfoObject>): MutableList<View> {
            val result = mutableListOf<View>()
            array.forEach {
                result.add(ContentViewMapper.mapInfoObjectArray(inflater, ObjectTypeContent(it.type, it.content)))
            }
            return result
        }
    }
}