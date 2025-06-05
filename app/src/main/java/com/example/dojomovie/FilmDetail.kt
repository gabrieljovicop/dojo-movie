package com.example.dojomovie

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import android.content.ContentValues

class FilmDetail : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var edtQuantity: EditText
    private lateinit var txtTotalPrice: TextView
    private lateinit var btnBuy: Button
    private var filmPrice: Int = 0
    private var filmId: Int = -1
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_detail)

        dbHelper = DBHelper(this)

        val prefs = getSharedPreferences("user", MODE_PRIVATE)
        userId = prefs.getInt("user_id", -1)
        filmId = intent.getIntExtra("film_id", -1)

        val txtTitle = findViewById<TextView>(R.id.txtFilmTitle)
        val txtPrice = findViewById<TextView>(R.id.txtFilmPrice)
        val imgCover = findViewById<ImageView>(R.id.imgFilmCover)
        txtTotalPrice = findViewById(R.id.txtTotalPrice)
        edtQuantity = findViewById(R.id.edtQuantity)
        btnBuy = findViewById(R.id.btnBuy)

        val cursor = dbHelper.getFilmById(filmId)
        if (cursor.moveToFirst()) {
            val title = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.KEY_FILM_TITLE))
            filmPrice = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.KEY_FILM_PRICE))
            val imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.KEY_FILM_IMAGE))

            txtTitle.text = title
            txtPrice.text = "Rp $filmPrice"
            updateTotalPrice()

            Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(imgCover)
        }
        cursor.close()

        edtQuantity.addTextChangedListener(object : android.text.TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) {
                updateTotalPrice()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        btnBuy.setOnClickListener {
            val qtyText = edtQuantity.text.toString()

            if (qtyText.isEmpty()) {
                Toast.makeText(this, "Quantity must be filled", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val qty = qtyText.toIntOrNull()
            if (qty == null || qty <= 0) {
                Toast.makeText(this, "Quantity must be a number and more than 0", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (userId == -1 || filmId == -1) {
                Toast.makeText(this, "Invalid user or film", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val values = ContentValues().apply {
                put(DBHelper.KEY_USER_ID_FK, userId)
                put(DBHelper.KEY_FILM_ID_FK, filmId)
                put(DBHelper.KEY_QUANTITY, qty)
            }

            val result = dbHelper.writableDatabase.insert(DBHelper.TABLE_TRANSACTIONS, null, values)
            if (result != -1L) {
                Toast.makeText(this, "Purchase successful!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Purchase failed!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateTotalPrice() {
        val qty = edtQuantity.text.toString().toIntOrNull() ?: 0
        val total = qty * filmPrice
        txtTotalPrice.text = "Total Price: Rp $total"
    }
}
