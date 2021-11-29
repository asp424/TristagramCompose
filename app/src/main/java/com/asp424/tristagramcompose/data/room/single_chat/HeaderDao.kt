package com.asp424.tristagramcompose.data.room.single_chat

import androidx.annotation.NonNull
import androidx.room.*

@Dao
interface HeaderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateMessage(item: HeaderModelRoom): Long

    @Query("select * from header_model where id = :id")
    fun getById(id: String): HeaderModelRoom?

    @Query("DELETE FROM header_model")
    fun deleteAllFromHeader()
}