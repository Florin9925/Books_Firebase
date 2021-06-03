package com.example.booksfirebase.database

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Operations {


    companion object {
        @JvmField
        val database =
            Firebase.database("https://books-57ad8-default-rtdb.europe-west1.firebasedatabase.app/").reference

        const val TAG: String = "Operations"
    }
}