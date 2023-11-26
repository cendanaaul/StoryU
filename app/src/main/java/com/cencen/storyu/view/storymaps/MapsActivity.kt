package com.cencen.storyu.view.storymaps

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.cencen.storyu.R
import com.cencen.storyu.data.Libraries
import com.cencen.storyu.data.models.RosterStory
import com.cencen.storyu.databinding.ActivityMapsBinding
import com.cencen.storyu.utility.ViewModelProviderFactory
import com.cencen.storyu.view.story.InformationStoryActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mapsViewModel: MapsViewModel
    private val llBoundBuild = LatLngBounds.builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.maps) as SupportMapFragment
        mapFragment.getMapAsync(this)

        initializeVM()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        initializeMemberStory()
        getPosition()
        applyMapsTheme()
    }

    private fun applyMapsTheme() {
        try {
            val successful =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.maps_style))
            if (!successful) {
                Log.e(TAG, "Style applying failed")
            }
        } catch (exc: Resources.NotFoundException) {
            Log.e(TAG, "Style not found. Error: ", exc)
        }
    }

    private fun initializeVM() {
        val fact: ViewModelProviderFactory = ViewModelProviderFactory.getInstance(this)
        mapsViewModel = ViewModelProvider(this, fact)[MapsViewModel::class.java]
    }

    private fun initializeMemberStory() {
        mapsViewModel.getMember().observe(this) {
            val tokens = "Bearer " + it.token
            mapsViewModel.getMemberStoryLoc(tokens).observe(this) {
                when (it) {
                    is Libraries.Loading -> showProcessing(true)
                    is Libraries.Success -> {
                        showMarkerStory(it.data.listStory)
                        showProcessing(false)
                    }

                    is Libraries.Error -> showToast(it.error)
                }
            }
        }
    }

    private fun showProcessing(load: Boolean) {
        binding.loadingProcess.visibility = if (load) View.VISIBLE else View.GONE
    }

    private fun showMarkerStory(mark: List<RosterStory>) {
        mark.forEach {
            val latLng = LatLng(it.lat!!, it.lon!!)
            val marks = mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(it.name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                    .alpha(0.6f)
                    .snippet(it.description)
            )
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            llBoundBuild.include(latLng)
            marks?.tag = it
            mMap.setOnInfoWindowClickListener {
                val intent = Intent(this, InformationStoryActivity::class.java).apply {
                    putExtra(InformationStoryActivity.EXTRA_DETAIL, it.tag as RosterStory)
                }
                startActivity(intent)
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { grant: Boolean ->
            if (grant) {
                getPosition()
            }
        }

    private fun getPosition() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "MapsActivity"
    }
}