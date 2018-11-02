package com.krasovsky.dima.demoproject.storage.model.basket

import kotlin.properties.Delegates

class BasketModel {
    var id: String = ""
    var items: MutableList<BasketItemModel> = mutableListOf()
    var totalCount: Int = 0
    var totalPrice: Float = 0f
}