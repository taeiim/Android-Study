package com.androidhuman.example.simplegithub.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.androidhuman.example.simplegithub.api.model.GithubRepo

@Database(entities = arrayOf(GithubRepo::class), version = 1)
abstract class SimpleGithubDatabase : RoomDatabase() {

    abstract fun searchHistoryDao(): SearchHistoryDao
}
