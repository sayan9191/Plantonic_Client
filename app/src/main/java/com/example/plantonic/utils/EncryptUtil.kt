package com.example.plantonic.utils

import java.util.Base64

class EncryptUtil {
    companion object {
        public fun encrypt(s : String) : String {
//            1f5b791c-38a4-4415-bfa8-398fdebd6fd8
//            1f5b791c-38a4-4415-bfa8-398fdebd6fd8
            var s1 = s.substring(s.length - 5, s.length) + s.substring(5, s.length - 5) + s.substring(0, 5)
            s1 = s1.substring(s1.length - 2, s1.length) + s1.substring(s1.length - 7, s1.length - 2) + s1.substring(7, s1.length - 7) + s1.substring(2, 7) + s1.substring(0, 2)

            return s1
        }

        public fun decrypt(s: String) : String {
            var s1 = s.substring(s.length - 2, s.length) + s.substring(s.length - 7, s.length - 2) + s.substring(7, s.length - 7) + s.substring(2, 7) + s.substring(0, 2)
            s1 = s1.substring(s1.length - 5, s1.length) + s1.substring(5, s1.length - 5) + s1.substring(0, 5)


            return s1
        }

    }
}