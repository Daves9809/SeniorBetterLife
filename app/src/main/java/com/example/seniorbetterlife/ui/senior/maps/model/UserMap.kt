package com.example.seniorbetterlife.ui.senior.maps.model

import java.io.Serializable

data class UserMap(val title:String, val places: List<Place>): Serializable {
    constructor() : this("", emptyList())
}