package com.capstone.karira.utils

fun createDotInNumber(number: String): String {
    return number.reversed().chunked(3).joinToString(separator = ".").reversed()
}

fun removeDotInNumber(number: String): String {
    return number.split(".").joinToString()
}