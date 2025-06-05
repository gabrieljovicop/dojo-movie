package com.example.dojomovie

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class History : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: TransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        dbHelper = DBHelper(this)
        recycler = findViewById(R.id.recyclerHistory)

        val prefs = getSharedPreferences("user", MODE_PRIVATE)
        val userId = prefs.getInt("user_id", -1)

        val cursor = dbHelper.getTransactionWithFilmByUserId(userId)
        val list = mutableListOf<TransactionItem>()

        while (cursor.moveToNext()) {
            val title = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.KEY_FILM_TITLE))
            val price = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.KEY_FILM_PRICE))
            val qty = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.KEY_QUANTITY))
            list.add(TransactionItem(title, price, qty))
        }

        adapter = TransactionAdapter(list)
        recycler.adapter = adapter
    }
}
