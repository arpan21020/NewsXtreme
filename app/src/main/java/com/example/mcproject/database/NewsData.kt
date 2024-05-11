package com.example.mcproject.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news_table")
data class NewsData(
    @PrimaryKey(autoGenerate = true)
    val id:Int?=null,
    val source:String?,
    val author:String?,
    val title:String?,
    val description:String?,
    val image:String?,
    val publishing_time:String?,
)
