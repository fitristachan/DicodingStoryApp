package com.dicoding.dicodingstoryapp.room

import androidx.paging.PagingSource
import androidx.room.*
import com.dicoding.dicodingstoryapp.retrofit.response.ListStoryItem

@Dao
interface StoriesPagingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStories(stories: List<ListStoryItem>)

    @Query("SELECT * FROM stories")
    fun getAllStories(): PagingSource<Int, ListStoryItem>

    @Query("DELETE FROM stories")
    fun deleteAll()
}