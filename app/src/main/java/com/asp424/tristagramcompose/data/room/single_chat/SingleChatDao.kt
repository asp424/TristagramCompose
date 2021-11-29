package com.asp424.tristagramcompose.data.room.single_chat

import androidx.room.*
import com.asp424.tristagramcompose.data.room.main_list.MainModelRoom

@Dao
interface SingleChatDao {
    @Query("delete from $TABLE_ID where id = :id")
    fun deleteMessageById(id: Int)

    @Delete
    fun deleteOneMessage(item: MessageModelRoom)

    @Query("select * from $TABLE_ID where `key` = :id" )
    fun getAllMessages(id: String): List<MessageModelRoom>

    @Query("select * from $TABLE_ID where `key` = :id LIMIT :count")
    fun getLastMessages(id: String, count: Int): List<MessageModelRoom>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateMessage(item: MessageModelRoom): Long

    @Query("DELETE FROM $TABLE_ID")
    fun deleteAllMessages()

    @Query("DELETE FROM $TABLE_ID WHERE id = :id")
    fun deleteMessage(id: String)
}