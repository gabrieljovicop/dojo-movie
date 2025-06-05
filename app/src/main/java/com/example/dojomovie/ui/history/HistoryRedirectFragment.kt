package com.example.dojomovie.ui.history

import android.content.Intent
import androidx.fragment.app.Fragment
import com.example.dojomovie.History
class HistoryRedirectFragment : Fragment() {
    override fun onResume() {
        super.onResume()
        startActivity(Intent(requireContext(), History::class.java))
        parentFragmentManager.popBackStack()
    }
}
