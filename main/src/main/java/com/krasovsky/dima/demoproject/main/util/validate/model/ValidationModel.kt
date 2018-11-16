package com.krasovsky.dima.demoproject.main.util.validate.model

import android.support.design.widget.TextInputLayout
import android.support.v7.widget.AppCompatEditText

import com.krasovsky.dima.demoproject.main.util.validate.model.param.interfaces.IValidateParam

import java.util.ArrayList


class ValidationModel(val layout: TextInputLayout, val edit: AppCompatEditText, val validateParams: List<IValidateParam>)
