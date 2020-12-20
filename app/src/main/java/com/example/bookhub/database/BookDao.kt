package com.example.bookhub.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BookDao {
    @Insert
    fun insertBook(bookEntity: BookEntity)

    @Delete
    fun deleteBook(bookEntity: BookEntity)
//Both these functions were taken care of by the ROOM library

    @Query("SELECT * FROM books")
    fun getAllBooks(): List<BookEntity>

    @Query("SELECT * FROM books WHERE book_id=:bookId")//All the operations are performed in the database class and are taken care of by the ROOM library
    //that's why we use interface here
    fun getBookById(bookId: String): BookEntity
}