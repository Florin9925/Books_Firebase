package com.example.booksfirebase.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.booksfirebase.R
import com.example.booksfirebase.adapters.BooksAdapter
import com.example.booksfirebase.database.Operations
import com.example.booksfirebase.interfaces.IActivityFragmentCommunication
import com.example.booksfirebase.models.Book
import com.example.booksfirebase.util.HashUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : Fragment() {
    private var activity: IActivityFragmentCommunication? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getBooks()

        add_book_button.setOnClickListener {
            val title = book_title_edit_text.text.toString().trim()
            val author = book_author_edit_text.text.toString().trim()
            val description = book_description_edit_text.text.toString().trim()

            if (title.isNotEmpty() && author.isNotEmpty() && description.isNotEmpty()) {
                addBook(Book(title, author, description))
            } else {
                Toast.makeText(context, "Missing fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IActivityFragmentCommunication) {
            this.activity = context
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }

    private fun getBooks() {

        Operations.database.child("books").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val books = ArrayList<Book>()

                snapshot.children.forEach {
                    val book = it.getValue(Book::class.java)
                    books.add(book as Book)
                }

                if (books.size > 0) {
                    books_recycler_view.adapter = BooksAdapter(books, activity)
                    books_recycler_view.layoutManager = LinearLayoutManager(context)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(Operations.TAG, "Failed to read value.", error.toException())
                throw error.toException()
            }
        })
    }

    private fun addBook(book: Book) {

        val key = HashUtil.makeKey(book.title + book.author)
        Operations.database.child("books").child(key).setValue(book).addOnSuccessListener {
            Log.d(Operations.TAG, "successfully wrote: $book")
        }.addOnFailureListener {
            Log.w(Operations.TAG, "failed writing: $book")
        }
    }
}