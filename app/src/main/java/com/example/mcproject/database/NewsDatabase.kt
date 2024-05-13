package com.example.mcproject.database

import Converters
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


val DATABASE_NAME: String= "news_database"

@Database(
    entities = [NewsData::class],
    version = 1
)

abstract class NewsDatabase:RoomDatabase() {
    abstract fun getDao():NewsDao

    companion object{
        @Volatile
        private var INSTANCE: NewsDatabase?=null

        fun getDatabase(context: Context):NewsDatabase{
            return INSTANCE?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NewsDatabase::class.java,
                    DATABASE_NAME
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}