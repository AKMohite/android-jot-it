package com.ak.jotit.feature.note.data.local

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: T)

    @Update
    fun update(entity: T)

    @Delete
    fun delete(entity: T)

}