package com.example.bookhub.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BookEntity::class],version = 1)//it becomes important to mention version, since sometimes, we may want to update our app, it won't let the app
//crash when the user updates to the newer version
abstract class BookDatabase: RoomDatabase(){
    abstract fun bookDao(): BookDao//Type database as parent and has a return type dao, this fun serves as a doorway for all dao operations
}