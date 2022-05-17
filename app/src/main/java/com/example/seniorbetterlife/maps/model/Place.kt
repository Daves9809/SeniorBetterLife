package com.example.seniorbetterlife.maps.model

import java.io.Serializable

data class Place(
    val title: String, val description: String,
    val latitude: Double, val longitude: Double,
    val urlOfGeopoint: String): Serializable{
    constructor(): this("","", Double.NaN,Double.NaN,"")
}
