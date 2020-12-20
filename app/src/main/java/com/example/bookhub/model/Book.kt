package com.example.bookhub.model

import android.widget.ImageView

data class Book (
    val bookId: String,
    val bookName: String,
    val bookAuthor: String,
    val bookRating: String,
    val price: String,
    val imgBookImage: String
    // it will hold the id of the image
)