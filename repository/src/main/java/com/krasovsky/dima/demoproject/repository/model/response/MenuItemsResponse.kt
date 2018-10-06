package com.krasovsky.dima.demoproject.repository.model.response

import com.krasovsky.dima.demoproject.repository.model.TypeMenuItems
import com.krasovsky.dima.demoproject.storage.model.BlockPage
import com.krasovsky.dima.demoproject.storage.model.MenuItemModel

class MenuItemsResponse(val type: TypeMenuItems, val data: List<MenuItemModel>)