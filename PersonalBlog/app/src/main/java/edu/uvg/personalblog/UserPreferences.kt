package edu.uvg.personalblog


import android.content.Context
import android.content.SharedPreferences

class UserPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    var firstName: String?
        get() = sharedPreferences.getString("first_name", null)
        set(value) = sharedPreferences.edit().putString("first_name", value).apply()

    var lastName: String?
        get() = sharedPreferences.getString("last_name", null)
        set(value) = sharedPreferences.edit().putString("last_name", value).apply()

    var email: String?
        get() = sharedPreferences.getString("email", null)
        set(value) = sharedPreferences.edit().putString("email", value).apply()

    var birthDate: String?
        get() = sharedPreferences.getString("birth_date", null)
        set(value) = sharedPreferences.edit().putString("birth_date", value).apply()
}
