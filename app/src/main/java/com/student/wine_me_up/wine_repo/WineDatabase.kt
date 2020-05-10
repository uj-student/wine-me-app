package com.student.wine_me_up.wine_repo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [WineEntity::class, WineReviewsEntity::class], version = 2, exportSchema = false)
@TypeConverters(WineTypeConverter::class)
abstract class WineDatabase : RoomDatabase() {
    abstract fun wineDao(): WineDao

    companion object {

        @Volatile
        private var INSTANCE: WineDatabase? = null

        fun getInstance(context: Context): WineDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        WineDatabase::class.java,
                        "wine_db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
