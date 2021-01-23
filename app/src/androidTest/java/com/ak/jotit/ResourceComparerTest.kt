package com.ak.jotit

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ResourceComparerTest {

    private val resourceComparer = ResourceComparer()

    @Test
    fun stringResourcesSameAsGivenString_returnsTrue() {

        val context = ApplicationProvider.getApplicationContext<Context>()
        val result = resourceComparer.isEqual(context, R.string.add_task, "Add Task")

        assertThat(result).isTrue()

    }

    @Test
    fun stringResourcesDifferentAsGivenString_returnsFalse() {

        val context = ApplicationProvider.getApplicationContext<Context>()
        val result = resourceComparer.isEqual(context, R.string.add_task, "Hello")

        assertThat(result).isFalse()

    }
}
