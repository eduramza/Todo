package com.eduramza.todoapp.database.source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.eduramza.todoapp.database.model.Todo

@Database(entities = [Todo::class], version = 1)
abstract class AppDatabase: RoomDatabase(){

    abstract fun todoDao(): TodoDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "Todo-Database.db"
        ).build()
    }
}