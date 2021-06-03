package com.example.booksfirebase.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.booksfirebase.databinding.FragmentSecondBinding
import com.example.booksfirebase.models.Book

class SecondFragment(private val book: Book) : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSecondBinding.bind(view)
        binding.bookFragmentTitleTextView.text = book.title
        binding.bookFragmentAuthorTextView.text = book.author
        binding.bookFragmentDescriptionTextView.text = book.description

    }

    companion object {
        @JvmStatic
        fun newInstance() = SecondFragment(Book("", "", ""))
    }
}