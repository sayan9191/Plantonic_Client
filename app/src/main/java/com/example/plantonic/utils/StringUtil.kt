package com.example.plantonic.utils

class StringUtil {

    companion object {
        fun getFormattedPrice(price : Long) : String{
            val p = price.toString().trim()
            var s = ""
            for (i in p.indices){
                if (i!= 0 && (p.length - i) % 3 == 0){
                    s = "$s,"
                }
                s += p[i]
            }

            return s
        }

        fun getFormattedPrice(price : String) : String{
            val p = price.trim()
            var s = ""
            for (i in p.indices){
                if (i!= 0 && (p.length - i) % 3 == 0){
                    s = "$s,"
                }
                s += p[i]
            }

            return s
        }
    }
}