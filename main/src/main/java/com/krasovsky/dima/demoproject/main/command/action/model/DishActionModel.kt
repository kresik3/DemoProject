package com.krasovsky.dima.demoproject.main.command.action.model

import android.support.v4.util.Pair
import android.view.View
import com.krasovsky.dima.demoproject.storage.model.dish.DishModel

class DishActionModel(val dish: DishModel, val options: Array<Pair<View, String>>)