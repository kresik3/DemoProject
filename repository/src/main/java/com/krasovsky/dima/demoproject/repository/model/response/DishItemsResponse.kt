package com.krasovsky.dima.demoproject.repository.model.response

import com.krasovsky.dima.demoproject.repository.model.enum_type.TypeLoaded
import com.krasovsky.dima.demoproject.storage.model.dish.DishModel

class DishItemsResponse(val type: TypeLoaded, val data: List<DishModel>)