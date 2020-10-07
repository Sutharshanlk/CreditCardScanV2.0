package com.example.administrator.creditcardscanv20

class clsValidations {
    fun monthDigit(month: Int): String? {
        var digitMonth: String? = null
        when (month) {
            1 -> digitMonth = "January"
            2 -> digitMonth = "February"
            3 -> digitMonth = "March"
            4 -> digitMonth = "April"
            5 -> digitMonth = "May"
            6 -> digitMonth = "June"
            7 -> digitMonth = "July"
            8 -> digitMonth = "July"
            9 -> digitMonth = "September"
            10 -> digitMonth = "October"
            11 -> digitMonth = "November"
            12 -> digitMonth = "December"
        }
        return digitMonth
    }

    fun cardType(cardNumber: String): String? {
        var ccType: String? = null
        if (cardNumber.length >= 13 && cardNumber.length <= 16 && cardNumber.startsWith("4")
        ) {
            ccType = "VISA"
        } else if (cardNumber.length == 16) {
            val prefix = cardNumber.substring(0, 2).toInt()
            if (prefix >= 51 && prefix <= 55) {
                ccType = "MASTER"
            }
        } else if (cardNumber.length == 15
            && (cardNumber.startsWith("34") || cardNumber
                .startsWith("37"))
        ) {
            ccType = "AMEX"
        } else if (cardNumber.length == 14) {
            val prefix = cardNumber.substring(0, 3).toInt()
            if (prefix >= 300 && prefix <= 305
                || cardNumber.startsWith("36")
                || cardNumber.startsWith("38")
            ) {
                ccType = "DINERS CLUB"
            }
        } else if (cardNumber.length == 16 && cardNumber.startsWith("6011")) {
            ccType = "DISCOVER"
        } else if (cardNumber.length == 16 && cardNumber.startsWith("3")
            || cardNumber.length == 15 && (cardNumber
                .startsWith("2131") || cardNumber
                .startsWith("1800"))
        ) {
            ccType = "JCB"
        }
        return ccType
    }
}