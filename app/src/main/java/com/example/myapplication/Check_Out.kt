package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.DecimalFormat

class Check_Out : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    lateinit var re: TextView
    lateinit var button: Button
    var r: String = ""
    lateinit var arrayList: ArrayList<String>
    lateinit var st: LatLng
    lateinit var en: LatLng
    var total :Double = 0.0
    @SuppressLint("MissingInflatedId")
    fun CalculationByDistance(StartP: LatLng, EndP: LatLng): Int {
        val Radius = 6371 // radius of earth in Km
        val lat1 = StartP.latitude
        val lat2 = EndP.latitude
        val lon1 = StartP.longitude
        val lon2 = EndP.longitude
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = (Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + (Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2)))
        val c = 2 * Math.asin(Math.sqrt(a))
        val valueResult = Radius * c
        val km = valueResult / 1
        val newFormat = DecimalFormat("####")
        val kmInDec: Int = Integer.valueOf(newFormat.format(km))
        val meter = valueResult % 1000
        val meterInDec: Int = Integer.valueOf(newFormat.format(meter))
        System.out.println(kmInDec)
        return kmInDec

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        arrayList = ArrayList()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_out)
        database = Firebase.database.reference
        auth = FirebaseAuth.getInstance()
        re = findViewById(R.id.log)
        r
        button = findViewById(R.id.register1)
        button.setOnClickListener{
            val intent = Intent(this@Check_Out,MainActivity3::class.java)
       startActivity(intent)
        }
        database = Firebase.database.reference
        FirebaseDatabase.getInstance()
            .getReference("users").child(auth.uid.toString()).child("Current")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        System.out.println(snapshot.value.toString())
                        if(snapshot.value.toString().equals("ss")){
                            val intent = Intent(this@Check_Out, Current_Location::class.java)
                        startActivity(intent)
                        }
                        else{
                        var l = snapshot.child("latitude").value.toString().toDouble()
                        var lo = snapshot.child("longitude").value.toString().toDouble()
                        st = LatLng(l, lo)
                        System.out.println(st)}
                    } else {

                    }

                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        database
            .child("users").child(auth.uid.toString()).child("cart")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (drinkSnapshot in snapshot.children) {
                            //System.out.println(drinkSnapshot.key.toString())
                            database.child("Location").child(drinkSnapshot.key.toString())
                                .child("name")
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.exists()) {

                                            r =
                                                r + "SuperMarket :" + snapshot.value.toString() + "\n"
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {

                                    }
                                })
                            database
                                .child("users").child(auth.uid.toString()).child("cart")
                                .child(drinkSnapshot.key.toString())
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.exists()) {

                                            for (drinkSnapshot in snapshot.children) {

                                                r =
                                                    r + "" + drinkSnapshot.child("name").value.toString() + "                      Quantity:" + drinkSnapshot.child(
                                                        "quntity"
                                                    ).value.toString() + "        Price:" + (drinkSnapshot.child(
                                                        "price"
                                                    ).value.toString()
                                                        .toInt() * drinkSnapshot.child("quntity").value.toString()
                                                        .toInt()) + "\n"
                                                total += (drinkSnapshot.child(
                                                    "price"
                                                ).value.toString()
                                                    .toInt() * drinkSnapshot.child("quntity").value.toString()
                                                    .toInt())
                                            }


                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {

                                    }
                                })
                            database.child("Location").child(drinkSnapshot.key.toString())
                                .child("Current")
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.exists()) {

                                            var l = snapshot.child("latitude").value.toString()
                                                .toDouble()
                                            var lo = snapshot.child("longitude").value.toString()
                                                .toDouble()
                                            en = LatLng(l,lo)
                                            var dist = CalculationByDistance(st, en)
                                            var p = dist * 10
                                            total += p
                                            r = r + "Delivery Fees :" + p + "\n"
                                            re.setText(r)
                                            r = r + "Total:      " + total + "\n"
                                            re.setText(r)

                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {

                                    }
                                })


                        }

                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })



    }

}