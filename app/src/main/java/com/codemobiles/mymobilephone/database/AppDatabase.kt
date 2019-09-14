package com.codemobiles.mymobilephone.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.codemobiles.mymobilephone.helper.DATABASE_NAME
import com.codemobiles.mymobilephone.converter.Converters

// #step2 [UserEntity::class]
@Database(entities = [MobileEntity::class,FavoriteEntity::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun mobileDao(): MobileDAO // #step3
    abstract fun favoriteDAO():favoriteDAO

    companion object {

        private val TAG: String by lazy { AppDatabase::class.java.simpleName }

        // For Singleton instantiation, visible to other threads.
        @Volatile
        private var instance: AppDatabase? = null


        fun getInstance(context: Context): AppDatabase {

            instance?.let {
                return it
                //ก็คือ if != null เหมือนข้างล่าง
            }

            /*if (instance != null){
            return instance!!

        }*/


            synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME // #step1
                ).addCallback(object : RoomDatabase.Callback() {
                    // onCreate will be called when the database is created for the first time
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        Log.d(TAG, "onCreate")
                    }

                })
                    .build().also {
                        instance = it
                        return instance!!
                    }
            }
        }

        fun destroyInstance() {
            instance = null
        }

        }


    }