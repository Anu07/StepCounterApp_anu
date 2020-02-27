package com.sdi.sdeiarchitecturemvvm.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
//
//@Database(entities = [Note::class], version = 1)
//abstract class RoomDb : RoomDatabase() {
//    abstract fun noteDao(): DaoAccess
//
//    companion object {
//
//        private var instance: RoomDb? = null
//
//        @Synchronized
//        fun getInstance(context: Context): RoomDb {
//            if (instance == null) {
//                instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    RoomDb::class.java, "note_database")
//                    .fallbackToDestructiveMigration()
//                    .build()
//            }
//            return instance as RoomDb
//        }
//    }
//}