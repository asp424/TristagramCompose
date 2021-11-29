package com.asp424.tristagramcompose.data.room.main_list

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "main_list")
class MainModelRoom(
    @NonNull
    @PrimaryKey
    var id: String = "",
    var state: String? = "",
    var lastMessage: String? = "",
    var lastMessageTime: String? = "",
    var fullName: String? = "",
    var photoUrl: String? = "",
    var type: String? = "",
    var was_reading: String? = "",
    var from: String? = "",
    var color: String? = "",
)


