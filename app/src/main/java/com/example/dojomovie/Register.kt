package com.example.dojomovie

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent

class Register : AppCompatActivity() {

    private lateinit var etPhone: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var tvLogin: TextView
    private lateinit var databaseHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize views
        etPhone = findViewById(R.id.etPhone)
        etPassword = findViewById(R.id.etPassword)
        btnRegister = findViewById(R.id.btnRegister)
        tvLogin = findViewById(R.id.tvLogin)

        // Initialize database helper
        databaseHelper = DBHelper(this)

        // Handle the Register button click
        btnRegister.setOnClickListener {
            val phone = etPhone.text.toString()
            val password = etPassword.text.toString()

            if (phone.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else if (databaseHelper.isUserRegistered(phone)) {
                Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show()
            } else {
                // Register the new user in the database
                databaseHelper.addUser(phone, password)
                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()

                // Redirect to Login Activity
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
                finish()  // Close the current activity
            }
        }

        // Handle the login link click (redirect to LoginActivity)
        tvLogin.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }
}
