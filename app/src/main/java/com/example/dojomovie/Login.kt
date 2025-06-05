package com.example.dojomovie

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Login : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var etPhone: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvRegister: TextView
    private lateinit var tvError: TextView
    private lateinit var databaseHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)

        etPhone = findViewById(R.id.etPhone)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvRegister = findViewById(R.id.tvRegister)
        tvError = findViewById(R.id.tvErrorMsg)

        databaseHelper = DBHelper(this)

        btnLogin.setOnClickListener {
            val phoneNumber = etPhone.text.toString()
            val password = etPassword.text.toString()

            if (phoneNumber.isEmpty()) {
                showError("Username must be filled!")
            } else if (password.isEmpty()) {
                showError("Password must be filled!")
            } else {
                val userId = getUserId(phoneNumber, password)
                if (userId != -1) {
                    val editor = sharedPreferences.edit()
                    editor.putInt("user_id", userId)
                    editor.putString("phone_number", phoneNumber)
                    editor.apply()

                    val intent = Intent(this, OTP::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    showError("Invalid credentials!")
                }
            }
        }

        tvRegister.setOnClickListener {
            // Redirect to Register activity
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }

    private fun showError(message: String) {
        tvError.text = message
        tvError.visibility = View.VISIBLE
    }

    private fun getUserId(phone: String, password: String): Int {
        val isUserValid = databaseHelper.validateUser(phone, password)
        return if (isUserValid) {
            val cursor = databaseHelper.getUserByPhone(phone)
            if (cursor != null && cursor.moveToFirst()) {
                cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.KEY_USER_ID))
            } else {
                -1
            }
        } else {
            -1
        }
    }
}