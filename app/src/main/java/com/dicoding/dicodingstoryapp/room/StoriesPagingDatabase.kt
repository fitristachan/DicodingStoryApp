package com.dicoding.dicodingstoryapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.dicodingstoryapp.retrofit.response.ListStoryItem

@Database(
    entities = [ListStoryItem::class, RemoteKeys::class], version = 2, exportSchema = false
)
abstract class StoriesPagingDatabase : RoomDatabase() {

    abstract fun storiesPagingDao(): StoriesPagingDao

    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: StoriesPagingDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): StoriesPagingDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    StoriesPagingDatabase::class.java,
                    "stories_database"
                ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
            }
        }
    }
}