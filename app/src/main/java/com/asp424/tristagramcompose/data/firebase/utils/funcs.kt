package com.asp424.tristagramcompose.data.firebase.utils

import com.asp424.tristagramcompose.data.firebase.models.*
import com.asp424.tristagramcompose.data.room.main_list.MainModelRoom
import com.asp424.tristagramcompose.data.room.single_chat.HeaderModelRoom
import com.asp424.tristagramcompose.data.room.single_chat.MessageModelRoom
import com.google.firebase.database.DataSnapshot

fun DataSnapshot.getUserModel(): UserModel =
    this.getValue(UserModel::class.java) ?: UserModel()

fun DataSnapshot.getHeaderModelRoom(): HeaderModelRoom =
    this.getValue(HeaderModelRoom::class.java) ?: HeaderModelRoom()

fun DataSnapshot.getMainModelRoom(): MainModelRoom =
    this.getValue(MainModelRoom::class.java) ?: MainModelRoom()

fun DataSnapshot.getMessageModelRoom(): MessageModelRoom =
    this.getValue(MessageModelRoom::class.java) ?: MessageModelRoom()

fun DataSnapshot.getPhoneContactModel(): PhoneContactModel =
    this.getValue(PhoneContactModel::class.java) ?: PhoneContactModel()

fun DataSnapshot.getMainListModel(): MainListModel =
    this.getValue(MainListModel::class.java) ?: MainListModel()





