package com.example.booksfirebase.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.booksfirebase.R
import com.example.booksfirebase.fragments.MainFragment
import com.example.booksfirebase.fragments.SecondFragment
import com.example.booksfirebase.interfaces.IActivityFragmentCommunication

class MainActivity : AppCompatActivity(), IActivityFragmentCommunication {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragment(MainFragment::class.java.name)

    }

    override fun openNextActivity() {
        // EMPTY
    }

    override fun replaceFragment(tag: String) = when (tag) {
        MainFragment::class.java.name -> {
            addMainFragment()
        }
        SecondFragment::class.java.name -> {
            addBookFragment()
        }
        else -> println("Invalid tag!")
    }

    private fun addBookFragment() {
        val fragmentManager = this.supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        val tag = SecondFragment::class.java.name
        val addTransaction = transaction.add(
            R.id.fragment_container, SecondFragment.newInstance(), tag
        )
        addTransaction.commit()
    }

    private fun addMainFragment() {
        val fragmentManager = this.supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        val tag = MainFragment::class.java.name
        val addTransaction = transaction.add(
            R.id.fragment_container, MainFragment.newInstance(), tag
        )
        addTransaction.commit()
    }
}