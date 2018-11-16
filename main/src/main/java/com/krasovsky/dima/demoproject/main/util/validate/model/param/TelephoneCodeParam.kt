package com.krasovsky.dima.demoproject.main.util.validate.model.param

import com.krasovsky.dima.demoproject.main.util.validate.model.param.interfaces.IValidateParam


class TelephoneCodeParam(override val error: String) : IValidateParam {

    val regex = "[0-9]{2}".toRegex()

    override fun validate(vararg text: String): Boolean {
        return if (text.size != 1) false else regex.matches(text[0])
    }
}
