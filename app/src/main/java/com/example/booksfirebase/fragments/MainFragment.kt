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
import com.example.booksfirebase.adapters.BooksAdapter
import com.example.booksfirebase.database.Operations
import com.example.booksfirebase.databinding.FragmentMainBinding
import com.example.booksfirebase.interfaces.IActivityFragmentCommunication
import com.example.booksfirebase.models.Book
import com.example.booksfirebase.util.HashUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class MainFragment : Fragment() {
    private var activity: IActivityFragmentCommunication? = null

    private var _binding: FragmentMainBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getBooks()

            binding.addBookButton.setOnClickListener {
            val title = binding.bookTitleEditText.text.toString().trim()
            val author = binding.bookAuthorEditText.text.toString().trim()
            val description = binding.bookDescriptionEditText.text.toString().trim()

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
                    binding.booksRecyclerView.adapter = BooksAdapter(books, activity)
                    binding.booksRecyclerView.layoutManager = LinearLayoutManager(context)
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