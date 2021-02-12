package com.openclassrooms.realestatemanager.models

import java.io.Serializable


data class SearchEstate(var estateType: String?,

                        var city: String?,
                        var minRooms: Int?,
                        var maxRooms: Int?,
                        var minSurface:Int?,
                        var maxSurface:Int?,
                        var minPrice:Double?,
                        var maxPrice:Double?,
                        var minUpOfSaleDate: Long?,
                        var maxOfSaleDate:Long?,
                        var photos:Boolean,
                        var schools:Boolean,
                        var stores:Boolean,
                        var park:Boolean,
                        var restaurants:Boolean,
                        var sold:Boolean


) :Serializable {
    constructor() : this("", "",null,null,null,
            null,null, null,null, null,
            false, false,false,false,false,false)
}