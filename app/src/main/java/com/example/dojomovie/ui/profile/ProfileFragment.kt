package com.example.dojomovie.ui.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.dojomovie.Login
import com.example.dojomovie.R

class ProfileFragment : Fragment() {

    private lateinit var tvPhoneNumber: TextView
    private lateinit var btnLogout: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        tvPhoneNumber = view.findViewById(R.id.tvPhoneNumber)
        btnLogout = view.findViewById(R.id.btnLogout)

        val prefs = requireContext().getSharedPreferences("user_prefs", 0)
        val phone = prefs.getString("phone_number", "Unknown")

        tvPhoneNumber.text = "Phone: $phone"

        btnLogout.setOnClickListener {
            showLogoutConfirmation()
        }

        return view
    }

    private fun showLogoutConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                logout()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun logout() {
        val prefs = requireContext().getSharedPreferences("user", 0).edit()
        prefs.clear()
        prefs.apply()

        val intent = Intent(requireContext(), Login::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
