package com.example.atmfinder

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import jxl.Sheet
import jxl.Workbook
import java.io.InputStream

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var PERMISSION_ID = 200
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    fun loadExcel(): ArrayList<MarkerInfo>{
        var arrayListResult = arrayListOf<MarkerInfo>()
        var am_assetmanager = assets
        var is_inputstream = am_assetmanager.open("VietinbankData.xls")
        var wb_workbook = Workbook.getWorkbook(is_inputstream)
        var sheet = wb_workbook.getSheet(0)
        var numRow  = sheet.rows
        var numColumn = sheet.columns

        for (i in 1 until numRow){
            var tempName = sheet.getCell(6,i).contents
            var tempLat = sheet.getCell(7,i).contents.toFloat()
            var tempLong = sheet.getCell(8,i).contents.toFloat()
            println("num row is " + numRow)
            println("vinhdeptrai " +i)
            var newMarker = MarkerInfo(tempLat,tempLong,tempName)
            print(newMarker.toString())
            arrayListResult.add(newMarker)
        }
        println("num row is " + numRow)
        return arrayListResult
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        var listMarkerInfo = loadExcel()
        for (i in 0 until listMarkerInfo.size){
            val temp = LatLng(listMarkerInfo[i].fLatitude.toDouble(),listMarkerInfo[i].fLongitude.toDouble())
            mMap.addMarker(MarkerOptions().position(temp).title(listMarkerInfo[i].strName))
        }
        //mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(listMarkerInfo[0].fLatitude.toDouble(),listMarkerInfo[0].fLongitude.toDouble())))
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Granted. Start getting the location information
            }
        }
    }
}

