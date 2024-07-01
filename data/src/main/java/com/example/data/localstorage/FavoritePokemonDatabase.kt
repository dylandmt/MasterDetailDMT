package com.example.data.localstorage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.utils.Constants.Companion.POKEMOS_DATABASE


@Database(entities = [FavoritePokemonEntity::class], version = 1, exportSchema = false)
abstract class FavoritePokemonDatabase : RoomDatabase(){
    abstract fun localStorageDao() : LocalStorageDAO

    companion object {
        @Volatile
        private var databaseInstance : FavoritePokemonDatabase ? = null

        @Synchronized
        fun getDatabaseInstance(context: Context): FavoritePokemonDatabase{
            if(databaseInstance == null){
                databaseInstance = Room.databaseBuilder(
                    context.applicationContext,
                    FavoritePokemonDatabase::class.java,
                    POKEMOS_DATABASE)
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build()
            }
            return databaseInstance!!
        }

        private val roomCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                //populateDatabase(instance!!)
            }
        }
    }
}