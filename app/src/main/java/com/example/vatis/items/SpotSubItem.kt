package com.example.vatis.items

import com.google.firebase.firestore.GeoPoint

data class SpotSubItem(var name:String ?=null, var type:String ?=null, var order:Pair<Long,Long>)
