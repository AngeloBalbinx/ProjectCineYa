package com.example.projectcineya

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.projectcineya.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private lateinit var cine: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cine = intent.getStringExtra("Cine").toString()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapa) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val latitud: Double
        val longitud: Double

        when(cine){
            "Cineplanet" -> {
                latitud = -12.050260714778329
                longitud = -77.033707483001


            }
            "Cinépolis" ->{
                latitud = -12.007161281350491
                longitud = -77.05898661631069

            }
            else -> {
                latitud = -12.07086063153215
                longitud = -77.01235133174217
            }
        }


        // Add a marker in Sydney and move the camera
        val sydney = LatLng(latitud, longitud)
        mMap.addMarker(MarkerOptions().position(sydney).title("Cine Seleccionado"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 17f))
    }
}