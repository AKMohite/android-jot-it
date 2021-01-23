package com.ak.jotit

import android.content.Context

// TODO delete if not needed created to learn android unit test
class ResourceComparer {

    fun isEqual(context: Context, resId: Int, resStr: String): Boolean {
        return context.getString(resId) == resStr
    }
}