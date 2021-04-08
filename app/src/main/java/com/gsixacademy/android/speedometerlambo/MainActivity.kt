package com.gsixacademy.android.speedometerlambo

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.ActivityCompat
import java.util.*

class MainActivity : AppCompatActivity(), LocationListener {

    var sw_metric: SwitchCompat? = null
    var tv_speed: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sw_metric = findViewById(R.id.sw_metric)
        tv_speed = findViewById(R.id.tv_speed)

        // Check for GPS permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1000)
        } else {
            // Start the program if the permission is granted
            doStuff()
        }
        updateSpeed(null)
        sw_metric!!.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked -> updateSpeed(null) })
    }

    override fun onLocationChanged(location: Location) {
        if (location != null) {
            val myLocation = CLocation(location, useMetricUnits())
            updateSpeed(myLocation)
        }
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}
    private fun doStuff() {
        val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)
        }
        Toast.makeText(this, "Waiting for GPS connection!", Toast.LENGTH_SHORT).show()
    }

    private fun updateSpeed(location: CLocation?) {
        var nCurrentSpeed = 0f
        if (location != null) {
            location.useMetricUnits = useMetricUnits()
            nCurrentSpeed = location.speed
        }
        val fmt = Formatter(StringBuilder())
        fmt.format(Locale.US, "%5.1f", nCurrentSpeed)
        var strCurrentSpeed = fmt.toString()
        strCurrentSpeed = strCurrentSpeed.replace("", "0")
        if (useMetricUnits()) {
            tv_speed!!.text = strCurrentSpeed + "km/h"
        } else {
            tv_speed!!.text = strCurrentSpeed + "mph"
        }
    }

    private fun useMetricUnits(): Boolean {
        return sw_metric!!.isChecked
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                doStuff()
            } else {
                finish()
            }
        }
    }
}