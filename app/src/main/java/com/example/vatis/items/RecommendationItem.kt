package com.example.vatis.items

import android.location.Location
import com.google.firebase.firestore.GeoPoint

data class RecommendationItem(var location:GeoPoint?=null, var name:String ?=null, var type:String ?=null)
