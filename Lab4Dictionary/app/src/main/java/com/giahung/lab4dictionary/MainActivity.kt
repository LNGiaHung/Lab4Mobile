package com.giahung.lab4dictionary

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var resultTextView: TextView
    private lateinit var dbHelper: DatabaseHelper
    private var isFirstRun = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchEditText = findViewById(R.id.search_edit_text)
        resultTextView = findViewById(R.id.result_text_view)

        // Initialize database helper
        dbHelper = DatabaseHelper(this)

        // Add sample data only on first run
        if (isFirstRun) {
            addSampleData()
            isFirstRun = false
        }

        // Set up search functionality
        setupSearch()
    }

    private fun setupSearch() {
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                true
            } else {
                false
            }
        }
    }

    private fun performSearch() {
        val searchText = searchEditText.text.toString().trim()
        
        if (searchText.isEmpty()) {
            Toast.makeText(this, "Please enter a word to search", Toast.LENGTH_SHORT).show()
            return
        }

        val results = dbHelper.searchWord(searchText)
        displayResults(results)
    }

    private fun addSampleData() {
        val initialWords = mapOf(
            "hello" to "Used as a greeting or to begin a phone conversation",
            "world" to "The earth, together with all of its countries and peoples",
            "dictionary" to "A book or electronic resource that lists words and their meanings",
            "help" to "Make it easier for (someone) to do something by offering one's services or resources",
            "computer" to "An electronic device for storing and processing data",
            "program" to "A series of coded software instructions to control operations of a computer",
            "android" to "A mobile operating system developed by Google",
            "kotlin" to "A modern programming language that makes developers happier",
            "java" to "An object-oriented programming language and computing platform",
            "database" to "A structured set of data held in a computer"
        )

        initialWords.forEach { (word, definition) ->
            dbHelper.insertWord(word, definition)
        }
    }

    private fun displayResults(results: List<Pair<String, String>>) {
        val resultString = when {
            results.isEmpty() -> "No matching words found."
            results.size == 1 -> {
                val (word, definition) = results.first()
                "📚 Word: $word\n\n📖 Definition:\n$definition"
            }
            else -> {
                "Found ${results.size} matching words:\n\n" +
                results.joinToString("\n\n") { (word, definition) ->
                    "📚 $word:\n$definition"
                }
            }
        }
        resultTextView.text = resultString
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }
}