package com.example.booksfirebase.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.booksfirebase.R
import com.example.booksfirebase.activities.MainActivity
import com.example.booksfirebase.database.Operations
import com.example.booksfirebase.databinding.BookItemBinding
import com.example.booksfirebase.fragments.SecondFragment
import com.example.booksfirebase.interfaces.IActivityFragmentCommunication
import com.example.booksfirebase.models.Book
import com.example.booksfirebase.util.HashUtil


class BooksAdapter(
    private val books: ArrayList<Book>,
    private val activity: IActivityFragmentCommunication?
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = BookItemBinding.bind(itemView)

        fun bind(book: Book) {
            binding.titleTextView.text = book.title
            binding.authorTextView.text = book.author
            binding.descriptionTextView.text = book.description
        }
    }

    private fun deleteFromDB(key: String, index: Int) {

        val databaseReference = Operations.database.child("books").child(key)
        databaseReference.setValue(null)
        books.removeAt(index)
        notifyItemRemoved(index)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = BookItemBinding.inflate(inflater, parent, false)
        val itemView = binding.root

        return BookViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = books[position]

        (holder as BookViewHolder).itemView.setOnClickListener {
            val myFragment: Fragment = SecondFragment(currentItem)
            (activity as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, myFragment).addToBackStack(null).commit()
        }
        holder.bind(currentItem)

        holder.binding.buttonDelete.setOnClickListener(null)
        holder.binding.buttonDelete.setOnClickListener {
            deleteFromDB(
                HashUtil.makeKey(holder.binding.titleTextView.text.toString() + holder.binding.authorTextView.text.toString()),
                position
            )
        }
    }

    override fun getItemCount() = books.size

}