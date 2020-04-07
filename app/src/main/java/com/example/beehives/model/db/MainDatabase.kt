package com.example.beehives.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.beehives.model.db.dao.GeneralDao
import com.example.beehives.model.db.entities.*

@Database(
    entities = [Apiary::class,
                Hive::class,
                Revision::class,
                BeeQueen::class,
                Todo::class], version = 1, exportSchema = false)

abstract class MainDatabase : RoomDatabase() {

    abstract fun generalDao(): GeneralDao

    companion object {
        @Volatile
        private var INSTANCE: MainDatabase? = null

        fun getDatabase(context: Context): MainDatabase {
            val tempInstance = INSTANCE
            if (tempInstance == null) {
                synchronized(MainDatabase::class) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        MainDatabase::class.java, "BeeHives"
                    ).build()
                    INSTANCE = instance
                    return instance
                }
            } else return tempInstance
        }
    }
}