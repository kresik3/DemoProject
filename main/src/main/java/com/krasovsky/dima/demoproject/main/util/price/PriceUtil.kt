package com.krasovsky.dima.demoproject.main.util.price

class PriceUtil {

    private val format = "%.2f"
    private val currency = "р."
    private val separator = " "

    fun parseToPrice(price: Float?): String {
        return String.format(format, price) + separator + currency
    }

    fun parseFromPrice(price: String): Float {
        val stringPrice = price.subSequence(0, price.indexOf(separator)).toString()
        return java.lang.Float.valueOf(stringPrice)
    }

}