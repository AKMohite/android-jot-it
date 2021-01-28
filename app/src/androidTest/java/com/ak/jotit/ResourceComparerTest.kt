package com.ak.jotit

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

class ResourceComparerTest {

    private lateinit var resourceComparer: ResourceComparer

    @Before
    fun setUp() {
        resourceComparer = ResourceComparer()
    }

    @After
    fun tearDown() {

    }

    @Test
    fun stringResourcesSameAsGivenString_returnsTrue() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val result = resourceComparer.isEqual(context, R.string.add_task, "Add Task")

        assertThat(result, `is` (true))

    }

    @Test
    fun stringResourcesDifferentAsGivenString_returnsFalse() {

        val context = ApplicationProvider.getApplicationContext<Context>()
        val result = resourceComparer.isEqual(context, R.string.add_task, "Hello")
        assertThat(result, `is` (false))

    }
}
