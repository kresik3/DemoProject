package com.krasovsky.dima.demoproject.main.view.model

import android.app.Application
import com.krasovsky.dima.demoproject.main.view.model.base.BaseAndroidViewModel
import com.krasovsky.dima.demoproject.main.view.controller.navigation.NavigationMenuController


class NavigationViewModel(application: Application) : BaseAndroidViewModel(application) {

    val controller: NavigationMenuController by lazy { NavigationMenuController() }

}