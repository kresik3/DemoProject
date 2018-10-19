package com.krasovsky.dima.demoproject.main.util.price

class PriceUtil {

    private val format = "%.2f"
    private val currency = "BYN"
    private val separator = " "

    fun parseToPrice(price: Float): String {
        return mapToComma(String.format(format, price) + separator + currency)
    }

    fun parseFromPrice(price: String): Float {
        val stringPrice = mapFromComma(price.subSequence(0, price.indexOf(separator)).toString())
        return java.lang.Float.valueOf(stringPrice)
    }

    private fun mapToComma(value: String): String {
        return value.replace(".", ",")
    }

    private fun mapFromComma(value: String): String {
        return value.replace(",", ".")
    }
}