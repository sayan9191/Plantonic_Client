package co.`in`.plantonic.utils

import android.content.SharedPreferences

class StorageUtil private constructor() {

    companion object{
        private var instance : StorageUtil? = null

        fun getInstance() : StorageUtil {
            return if (instance != null)
                instance as StorageUtil
            else{
                instance = StorageUtil()
                instance as StorageUtil
            }
        }
    }

    var sharedPref: SharedPreferences? = null

    var token: String?
        get() {
            return sharedPref?.getString("token", "")
        }
        set(value) {
            sharedPref?.edit()?.putString("token", value)?.apply()
        }
}