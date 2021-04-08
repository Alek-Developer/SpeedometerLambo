package com.gsixacademy.android.speedometerlambo

import android.location.Location

class CLocation @JvmOverloads constructor(
    location: Location?,
    bUseMetricUnits: Boolean = true
) :
    Location(location) {
    var useMetricUnits = false

    override fun distanceTo(dest: Location): Float {
        var nDistance = super.distanceTo(dest)
        if (!useMetricUnits) {
            // Convert meters to feet
            nDistance = nDistance * 3.28083989501312f
        }
        return nDistance
    }

    override fun getAltitude(): Double {
        var nAltitude = super.getAltitude()
        if (!useMetricUnits) {
            // Convert meters to feet
            nAltitude = nAltitude * 3.28083989501312
        }
        return nAltitude
    }

    override fun getSpeed(): Float {
        var nSpeed = super.getSpeed() * 3.6f
        if (!useMetricUnits) {
            // Convert meters/second to miles/hour
            nSpeed = super.getSpeed() * 2.23693629f
        }
        return nSpeed
    }

    override fun getAccuracy(): Float {
        var nAccuracy = super.getAccuracy()
        if (!useMetricUnits) {
            // Convert meters to feet
            nAccuracy = nAccuracy * 3.28083989501312f
        }
        return nAccuracy
    }

    init {
        useMetricUnits = bUseMetricUnits
    }
}