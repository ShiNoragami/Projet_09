package com.openclassrooms.realestatemanager.models

import java.io.Serializable

data class UriList(
        val photoList: java.util.ArrayList<String> = ArrayList()
) : Serializable
