package com.asp424.tristagramcompose.data.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.asp424.tristagramcompose.data.room.main_list.MainListDao
import com.asp424.tristagramcompose.data.room.main_list.MainModelRoom
import com.asp424.tristagramcompose.data.room.single_chat.HeaderDao
import com.asp424.tristagramcompose.data.room.single_chat.HeaderModelRoom
import com.asp424.tristagramcompose.data.room.single_chat.MessageModelRoom
import com.asp424.tristagramcompose.data.room.single_chat.SingleChatDao

@Database(entities = [MainModelRoom::class, MessageModelRoom::class, HeaderModelRoom::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun mainListDao(): MainListDao
    abstract fun singleChatDao(): SingleChatDao
    abstract fun headerDao(): HeaderDao
}