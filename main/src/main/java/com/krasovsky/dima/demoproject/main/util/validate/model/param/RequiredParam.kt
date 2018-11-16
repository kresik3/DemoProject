package com.krasovsky.dima.demoproject.main.util.validate.model.param

import com.krasovsky.dima.demoproject.main.util.validate.model.param.interfaces.IValidateParam


class RequiredParam(override val error: String) : IValidateParam {

    override fun validate(vararg text: String): Boolean {
        return if (text.size != 1) false else !text[0].isEmpty()
    }
}
