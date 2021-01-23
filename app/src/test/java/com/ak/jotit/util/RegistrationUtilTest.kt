package com.ak.jotit.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RegistrationUtilTest {

    @Test
    fun `empty username returns false`() {
        val result = RegistrationUtil.validateRegistrationInput(
            "",
            "123",
            "123"
        )

        assertThat(result).isFalse()
    }

    @Test
    fun `valid username and password returns true`() {
        val result = RegistrationUtil.validateRegistrationInput(
            "Jon",
            "123",
            "123"
        )

        assertThat(result).isTrue()
    }

    @Test
    fun `username already exist returns false`() {
        val result = RegistrationUtil.validateRegistrationInput(
            "Qwerty",
            "123",
            "123"
        )

        assertThat(result).isFalse()
    }

    @Test
    fun `empty password returns false`() {
        val result = RegistrationUtil.validateRegistrationInput(
            "Qwerty",
            "",
            ""
        )

        assertThat(result).isFalse()
    }

    @Test
    fun `password repeated returns false`() {
        val result = RegistrationUtil.validateRegistrationInput(
            "Dan",
            "123",
            "1234"
        )

        assertThat(result).isFalse()
    }

    @Test
    fun `password length less than 2 returns false`() {
        val result = RegistrationUtil.validateRegistrationInput(
            "Jon",
            "1",
            "123"
        )

        assertThat(result).isFalse()
    }


}