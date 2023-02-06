package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SuperMarketLocation : AppCompatActivity() {
    lateinit var lat : EditText
    lateinit var lon : EditText
    lateinit var Name : EditText
    lateinit var latt :LatLng
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    lateinit var button : Button
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_super_market_location)
        lat = findViewById(R.id.lat)
        lon = findViewById(R.id.lon)
        Name = findViewById(R.id.SuperName)
        button = findViewById(R.id.sub)
        database = Firebase.database.reference
        auth = FirebaseAuth.getInstance()

button.setOnClickListener() {
    val x = Name.text.toString()
    val lat1 = lat.text.toString().toDouble()
    val lonn = lon.text.toString().toDouble()
    latt = LatLng(lat1, lonn)
    if(x.isEmpty()){
        Toast.makeText(applicationContext,"Please enter all the fields", Toast.LENGTH_SHORT).show()
    }
    else{
    database.child("Location").child(auth.uid.toString()).child("Current").setValue(latt)
//        database.child("Locations").child("Current").setValue(latt)
//        database.child("Locations").child("Current").setValue(latt)
    database.child("Location").child(auth.uid.toString()).child("name").setValue(x)
    database.child("users").child(auth.uid.toString()).child("super").setValue(latt)
    val intent = Intent(this@SuperMarketLocation,MainActivity3::class.java)
    startActivity(intent)

}
    }
}
}