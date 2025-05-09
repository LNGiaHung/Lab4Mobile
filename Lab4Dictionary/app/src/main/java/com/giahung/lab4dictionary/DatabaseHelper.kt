package com.giahung.lab4dictionary

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "DictionaryDB"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "dictionary"
        private const val COLUMN_ID = "id"
        private const val COLUMN_WORD = "word"
        private const val COLUMN_DEFINITION = "definition"
    }

    override fun onCreate(db: SQLiteDatabase) {
        try {
            val createTable = """
                CREATE TABLE $TABLE_NAME (
                    $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $COLUMN_WORD TEXT UNIQUE,
                    $COLUMN_DEFINITION TEXT
                )
            """.trimIndent()
            db.execSQL(createTable)
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Error creating database", e)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertWord(word: String, definition: String): Boolean {
        return try {
            val db = this.writableDatabase
            val values = ContentValues().apply {
                put(COLUMN_WORD, word.lowercase())
                put(COLUMN_DEFINITION, definition)
            }
            db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE)
            true
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Error inserting word", e)
            false
        }
    }

    fun searchWord(searchText: String): List<Pair<String, String>> {
        val results = mutableListOf<Pair<String, String>>()
        val db = this.readableDatabase
        
        try {
            // First try exact match (case insensitive)
            var cursor = db.query(
                TABLE_NAME,
                arrayOf(COLUMN_WORD, COLUMN_DEFINITION),
                "$COLUMN_WORD LIKE ?",
                arrayOf(searchText.lowercase()),
                null,
                null,
                "$COLUMN_WORD ASC"
            )

            if (cursor.moveToFirst()) {
                do {
                    val word = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WORD))
                    val definition = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DEFINITION))
                    results.add(Pair(word, definition))
                } while (cursor.moveToNext())
            } else {
                // If no exact match, try substring match
                cursor.close()
                cursor = db.query(
                    TABLE_NAME,
                    arrayOf(COLUMN_WORD, COLUMN_DEFINITION),
                    "$COLUMN_WORD LIKE ?",
                    arrayOf("%${searchText.lowercase()}%"),
                    null,
                    null,
                    "$COLUMN_WORD ASC"
                )

                if (cursor.moveToFirst()) {
                    do {
                        val word = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WORD))
                        val definition = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DEFINITION))
                        results.add(Pair(word, definition))
                    } while (cursor.moveToNext())
                }
            }
            cursor.close()
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Error searching word", e)
        }
        
        return results
    }

    fun clearAllWords() {
        try {
            val db = this.writableDatabase
            db.delete(TABLE_NAME, null, null)
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Error clearing database", e)
        }
    }
}