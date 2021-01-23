package com.ak.jotit.util

// TODO delete if not needed created to learn android unit test
object RegistrationUtil {

    val existingUsers = listOf<String>("Dan", "Qwerty")

    fun validateRegistrationInput(
        userName: String,
        password: String,
        confirmPassword: String
    ): Boolean{

        if (userName.isEmpty() || password.isEmpty())
            return false
        if (userName in existingUsers)
            return false
        if (password != confirmPassword)
            return false
        if (password.count { it.isDigit() } < 2)
            return false

        return true
    }
}