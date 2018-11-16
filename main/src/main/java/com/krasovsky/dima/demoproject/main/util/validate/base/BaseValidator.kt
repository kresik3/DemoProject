package com.krasovsky.dima.demoproject.main.util.validate.base

import android.support.design.widget.TextInputLayout
import com.krasovsky.dima.demoproject.main.util.validate.model.ValidationModel

open class BaseValidator {

    protected fun validate(model: ValidationModel): Boolean {
        var result = true
        for (param in model.validateParams) {
            result = param.validate(model.edit.text.toString())
            setError(if (!result) param.error else null, model.layout)
            if (!result) break
        }
        return result
    }

    protected fun setError(message: String?, layout: TextInputLayout) {
        layout.error = message
    }
}
