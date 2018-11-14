package com.krasovsky.dima.demoproject.repository.model.response

import com.krasovsky.dima.demoproject.repository.model.TypeItems
import com.krasovsky.dima.demoproject.storage.model.MenuItemModel
import com.krasovsky.dima.demoproject.storage.model.page.BlockInfoObject

class InfoObjectsResponse(val type: TypeItems, val data: List<BlockInfoObject>)