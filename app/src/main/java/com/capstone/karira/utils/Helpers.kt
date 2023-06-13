package com.capstone.karira.utils

fun createDotInNumber(number: String): String {
    return number.reversed().chunked(3).joinToString(separator = ".").reversed()
}

fun removeDotInNumber(number: String): String {
    return number.split(".").joinToString()
}

fun convertStringDescTextToNumber(numberText: String): List<Int> {
    var firstNumber = 0
    var secondNumber = 0
    val splittedNumber = numberText.split(" - ")
    if (splittedNumber[0].contains("Ribu")) {
        firstNumber = (splittedNumber[0].split(" ")[0].toFloat() * 1000).toInt()
        secondNumber = (splittedNumber[1].split(" ")[0].toFloat() * 1000000).toInt()
    } else if (splittedNumber[1].contains("Ribu")) {
        firstNumber = (splittedNumber[0].split(" ")[0].toFloat() * 1000).toInt()
        secondNumber = (splittedNumber[1].split(" ")[0].toFloat() * 1000).toInt()
    } else if (splittedNumber[1].contains("Juta")) {
        firstNumber = (splittedNumber[0].split(" ")[0].toFloat() * 1000000).toInt()
        secondNumber = (splittedNumber[1].split(" ")[0].toFloat() * 1000000).toInt()
    }
    return listOf(firstNumber, secondNumber)
}