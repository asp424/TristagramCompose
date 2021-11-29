package com.asp424.tristagramcompose.data.room.single_chat

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
const val TABLE_ID = "messages_model"
@Entity(tableName = TABLE_ID)
class MessageModelRoom(
    @NonNull
    @PrimaryKey
    var id: String = "",
    var from: String? = "",
    var who: String? = "",
    var text: String? = "",
    var was_reading: String? = "",
    var key: String? = "",
    var timeStamp: String? = "",
    var userName: String? = ""
)

