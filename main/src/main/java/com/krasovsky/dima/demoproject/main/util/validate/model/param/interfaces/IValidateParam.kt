package com.krasovsky.dima.demoproject.main.util.validate.model.param.interfaces

interface IValidateParam {
    val error: String
    fun validate(vararg text: String): Boolean
}
