package com.asp424.tristagramcompose.data.room.main_list

import androidx.room.*

@Dao
interface MainListDao {
    @Query("delete from main_list where id = :id")
    fun deleteById(id: Int)

    @Delete
    fun delete(item: MainModelRoom)

    @Query("select * from main_list")
    fun getAllItems(): List<MainModelRoom>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateItem(item: MainModelRoom): Long

    @Query("DELETE FROM main_list")
    fun deleteAllFromMainList()

    @Query("DELETE FROM main_list WHERE id = :id")
    fun deleteItem(id: String)
}
