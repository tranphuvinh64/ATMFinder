package com.example.atmfinder

class MarkerInfo {

     var fLatitude : Float

     var fLongitude : Float


     var strName : String


    constructor(_latitude : Float, _longitude : Float, _name : String) {
        this.fLatitude = _latitude
        this.fLongitude = _longitude
        this.strName = _name
    }
    override fun toString(): String {
        return "latitude = " + this.fLatitude + "-" + "longitude = " + this.fLongitude + "-" + "Name = " + this.strName
    }
}