package com.asp424.tristagramcompose.data.room.single_chat

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "header_model")
class HeaderModelRoom(
    @NonNull
    @PrimaryKey
    var id: String = "",
    var fullname: String? = "",
    var state: String? = "",
    var photoUrl: String? = "",
    var token: String? = "",
    var color: String? = ""
    )