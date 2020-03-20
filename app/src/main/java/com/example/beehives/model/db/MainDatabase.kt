package com.example.beehives.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.beehives.model.db.dao.GeneralDao
import com.example.beehives.model.db.entities.Apiary
import com.example.beehives.model.db.entities.Hive
import com.example.beehives.model.db.entities.Revision

@Database(
    entities = [Apiary::class,
                Hive::class,
                Revision::class], version = 1, exportSchema = false)

abstract class MainDatabase : RoomDatabase() {

    abstract fun hiveDao(): GeneralDao

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