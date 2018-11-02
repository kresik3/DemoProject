package com.krasovsky.dima.demoproject.repository.model.response

import com.krasovsky.dima.demoproject.repository.model.TypeItems
import com.krasovsky.dima.demoproject.storage.model.MenuItemModel

class MenuItemsResponse(val type: TypeItems, val data: List<MenuItemModel>)