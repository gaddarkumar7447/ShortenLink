package com.example.myapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ShortenUrlTable")
data class ShortenUrl(
    val date: String,
    val fullLink: String,
    val shortLink: String,
    val status: Int,

    @PrimaryKey
    val title: String
)