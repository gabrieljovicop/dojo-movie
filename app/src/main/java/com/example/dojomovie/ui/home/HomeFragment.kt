package com.example.dojomovie.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dojomovie.databinding.FragmentHomeBinding
import com.example.dojomovie.R
import com.example.dojomovie.Film

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

import com.example.dojomovie.DBHelper
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import android.util.Log

class HomeFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return binding.root
        fetchFilmsFromAPI()

    }

    private fun fetchFilmsFromAPI() {
        val url = "https://api.npoint.io/66cce8acb8f366d2a508"
        val requestQueue = Volley.newRequestQueue(requireContext())

        val stringRequest = StringRequest(
            com.android.volley.Request.Method.GET, url,
            { response ->
                try {
                    val jsonArray = JSONArray(response)
                    val dbHelper = DBHelper(requireContext())

                    for (i in 0 until jsonArray.length()) {
                        val filmObj = jsonArray.getJSONObject(i)

                        val title = filmObj.getString("film_title")
                        val image = filmObj.getString("film_image")
                        val price = filmObj.getInt("film_price")

                        dbHelper.addFilm(title, image, price)
                    }

                    Log.d("VolleySuccess", "Data film berhasil disimpan ke DB")
                } catch (e: Exception) {
                    Log.e("ParseError", "Gagal parse JSON: ${e.message}")
                }
            },
            { error ->
                Log.e("VolleyError", "Gagal mengambil data: ${error.message}")
            })

        requestQueue.add(stringRequest)
        loadFilmsToRecyclerView()
    }

    private fun loadFilmsToRecyclerView() {
        val dbHelper = DBHelper(requireContext())
        val cursor = dbHelper.getAllFilms() // Buat fungsi ini jika belum

        val filmList = mutableListOf<Film>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.KEY_FILM_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.KEY_FILM_TITLE))
            val image = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.KEY_FILM_IMAGE))
            val price = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.KEY_FILM_PRICE))

            filmList.add(Film(id, title, image, price))
        }
        cursor.close()

        val adapter = FilmAdapter(requireContext(), filmList)
        binding.recyclerFilms.adapter = adapter
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val dojoLocation = LatLng(-6.2088, 106.8456)
        googleMap.addMarker(MarkerOptions().position(dojoLocation).title("DoJo Movie"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dojoLocation, 15f))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
