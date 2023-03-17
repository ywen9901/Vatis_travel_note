package com.example.vatis

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vatis.adapters.RecommendationAdapter
import com.example.vatis.items.RecommendationItem
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddSpotActivity : AppCompatActivity(),CellClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recommendationArrayList : ArrayList<RecommendationItem>
    private lateinit var recommendationAdapter: RecommendationAdapter
    //    private lateinit var db: FirebaseFirestore
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_spot)


        Places.initialize(applicationContext, "AIzaSyCyP17H2sXocWjrzCwQ5MctHtckO9d_Lu0")
        val placesClient = Places.createClient(this)

        // Initialize the AutocompleteSupportFragment.
        val autocompleteFragment =
                supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                        as AutocompleteSupportFragment

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.LAT_LNG, Place.Field.NAME,Place.Field.TYPES))

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.
                var str = place.types?.toList()?.get(0).toString()

                var lat = place.latLng?.latitude
                var lng = place.latLng?.longitude
                val geoPoint = GeoPoint(lat?:0.0,lng?:0.0)
                var locationType = "scenic"
                if(str == "LODGING"){
                    locationType = "hotel"
                }
                else if(str == "BAKERY" ||str == "BAR" || str == "CAFE"||str == "RESTAURANT"){
                    locationType = "restaurant"
                }
                else{
                    locationType = "scenic"
                }
                addPlaceToPlan(place.name.toString(),geoPoint,locationType)
            }

            override fun onError(p0: Status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred")
            }
        })
        //bakery bar cafe restaurant
        //lodging

        val btn_restaurant = findViewById<ImageButton>(R.id.restaurant)
        btn_restaurant.setOnClickListener {

            recyclerView = findViewById(R.id.add_spot_recyclerview)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.setHasFixedSize(true)

            recommendationArrayList = arrayListOf()
            recommendationAdapter = RecommendationAdapter(recommendationArrayList,this)
            recyclerView.adapter = recommendationAdapter
            EventChangeListener("restaurant")
            Toast.makeText(this, "restaurant", Toast.LENGTH_SHORT).show()
        }

        val btn_hotel = findViewById<ImageButton>(R.id.hotel)
        btn_hotel.setOnClickListener {
            recyclerView = findViewById(R.id.add_spot_recyclerview)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.setHasFixedSize(true)

            recommendationArrayList = arrayListOf()
            recommendationAdapter = RecommendationAdapter(recommendationArrayList,this)
            recyclerView.adapter=recommendationAdapter
            EventChangeListener("hotel")
            Toast.makeText(this, "hotel", Toast.LENGTH_SHORT).show()
        }

        val btn_scenic = findViewById<ImageButton>(R.id.scenic)
        btn_scenic.setOnClickListener {
            recyclerView = findViewById(R.id.add_spot_recyclerview)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.setHasFixedSize(true)

            recommendationArrayList = arrayListOf()
            recommendationAdapter = RecommendationAdapter(recommendationArrayList,this)
            recyclerView.adapter = recommendationAdapter
            EventChangeListener("scenic")
            Toast.makeText(this, "scenic", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCellClickListener(data: RecommendationItem) {
        Toast.makeText(this,data.name, Toast.LENGTH_SHORT).show()
        Toast.makeText(this,data.type, Toast.LENGTH_SHORT).show()
        Toast.makeText(this,data.location.toString(), Toast.LENGTH_SHORT).show()
        addPlaceToPlan(data.name.toString(),data.location!!,data.type.toString())
    }

    private fun addPlaceToPlan(name: String, location: GeoPoint, type: String){
        val test1 = hashMapOf(
                "name" to name,
                "location" to location,
                "type" to type
        )

        db.collection("addtest")
                .add(test1)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
    }

    private fun EventChangeListener(locationType: String){
//        db = FirebaseFirestore.getInstance()
        db.collection("users")
            .document("python_test@gmail.com")
            .collection("folder1")
            .document("file1")
            .collection("recommendations")
            .addSnapshotListener(object: EventListener<QuerySnapshot> {
                override fun onEvent(
                        value: QuerySnapshot?,
                        error: FirebaseFirestoreException?) {
                    if(error!=null){
                        Log.e("Firestore error",error.message.toString())
                        return
                    }
                    for(dc: DocumentChange in value?.documentChanges!!){
                        if(dc.type== DocumentChange.Type.ADDED){
                            if(dc.document.toObject(RecommendationItem::class.java).type == locationType){
                                recommendationArrayList.add(dc.document.toObject(RecommendationItem::class.java))
                            }
                        }
                    }
                    recommendationAdapter.notifyDataSetChanged()
                }
            })
    }
}