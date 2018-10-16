package com.krasovsky.dima.demoproject.repository.model.response

import com.krasovsky.dima.demoproject.repository.model.TypeMenuItems
import com.krasovsky.dima.demoproject.storage.model.dish.DishModel

class DishItemsResponse(val type: TypeMenuItems, val data: List<DishModel>)