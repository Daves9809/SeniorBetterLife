package com.example.seniorbetterlife.helpPart.model

import android.location.Address

data class UserAddress(val address: String,
                       val lattitude: Double,
                       val longtitude: Double
){
    constructor() : this("",Double.NaN,Double.NaN)

    fun getUserAddressFromGeoLocation(geoLocation: List<Address>): UserAddress {
        val address = geoLocation[0].getAddressLine(0)
        val latitude = geoLocation[0].latitude
        val longitude = geoLocation[0].longitude
        return(UserAddress(address,latitude,longitude))
    }
}
