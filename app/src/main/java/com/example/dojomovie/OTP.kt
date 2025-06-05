package com.example.dojomovie

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class OTP : AppCompatActivity() {

    private lateinit var etOtp: EditText
    private lateinit var btnVerifyOtp: Button
    private lateinit var tvResendOtp: TextView
    private lateinit var tvOtpError: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        etOtp = findViewById(R.id.etOtp)
        btnVerifyOtp = findViewById(R.id.btnVerifyOtp)
        tvResendOtp = findViewById(R.id.tvResendOtp)
        tvOtpError = findViewById(R.id.tvOtpError)

        btnVerifyOtp.setOnClickListener {
            val otp = etOtp.text.toString()

            if (otp.isEmpty()) {
                showError("OTP must be filled!")
            } else if (otp == "123456") {
                Toast.makeText(this, "OTP Verified", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                showError("Invalid OTP!")
            }
        }

        tvResendOtp.setOnClickListener {
            Toast.makeText(this, "OTP Sent Again", Toast.LENGTH_SHORT).show()
            // Reset OTP field
            etOtp.text.clear()
        }
    }

    private fun showError(message: String) {
        tvOtpError.text = message
        tvOtpError.visibility = View.VISIBLE
    }
}
