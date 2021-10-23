package com.ak.jotit.feature.note.data.local

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: T): Long

    @Update
    suspend fun update(entity: T): Int

    @Delete
    suspend fun delete(entity: T): Int

}