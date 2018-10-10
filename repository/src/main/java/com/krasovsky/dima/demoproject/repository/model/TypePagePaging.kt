package com.krasovsky.dima.demoproject.repository.model

enum class TypePagePaging() {
    NEED_LOAD(),
    NOT_NEED_LOAD(),
    ERROR_LOAD_HISTORY(),
    CLEAR_DB(),
    ERROR_LOADED()
}