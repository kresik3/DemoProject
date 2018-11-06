package com.krasovsky.dima.demoproject.main.util.validate

import com.krasovsky.dima.demoproject.main.util.validate.base.BaseValidator
import com.krasovsky.dima.demoproject.main.util.validate.model.ValidationModel


class PaymentValidator : BaseValidator() {

    fun validateName(name: ValidationModel): Boolean {
        return validate(name)
    }

    fun validateTelephone(code: ValidationModel, telephone: ValidationModel): Boolean {
        var result = validateCode(code)
        if (result) {
            result = validate(telephone)
        }
        return result
    }

    private fun validateCode(code: ValidationModel): Boolean {
        return validate(code)
    }

    fun validateAddress(address: ValidationModel): Boolean {
        return validate(address)
    }


}
