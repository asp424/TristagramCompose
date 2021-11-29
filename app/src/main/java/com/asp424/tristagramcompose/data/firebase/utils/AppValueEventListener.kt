package com.asp424.tristagramcompose.data.firebase.utils

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class AppValueEventListener (val OnSuccess:(DataSnapshot) ->Unit): ValueEventListener{
    override fun onDataChange(snapshot: DataSnapshot) {
OnSuccess(snapshot)
    }
    override fun onCancelled(error: DatabaseError) {
    }
}