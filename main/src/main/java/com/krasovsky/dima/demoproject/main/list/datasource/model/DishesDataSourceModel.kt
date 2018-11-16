package com.krasovsky.dima.demoproject.main.list.datasource.model

import com.krasovsky.dima.demoproject.main.list.datasource.model.base.BaseDataSourceModel
import io.reactivex.disposables.CompositeDisposable

class DishesDataSourceModel<T>(val categoryId: String,
                               disposable: CompositeDisposable, manager: T)
    : BaseDataSourceModel<T>(disposable, manager)