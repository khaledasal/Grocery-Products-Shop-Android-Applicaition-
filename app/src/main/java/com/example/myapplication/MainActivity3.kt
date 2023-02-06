package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity3 : AppCompatActivity() {
    lateinit var button1: Button
    lateinit var button2 : Button
    lateinit var button3 : Button
    lateinit var button4 : Button
    lateinit var button5: Button
    lateinit var button6: Button
    lateinit var button7: Button
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        button1 = findViewById(R.id.Maps)
        button2 = findViewById(R.id.To)
        button3 = findViewById(R.id.signout)
        button4 = findViewById(R.id.seller)
        button5 = findViewById(R.id.Buyer1)
        button6 = findViewById(R.id.CC)
        button7 = findViewById(R.id.SuperMarketLocationbtn)
        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference
        FirebaseDatabase.getInstance()
            .getReference("users").child(auth.uid.toString()).child("Current")
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(!snapshot.exists())
                    {   System.out.println("Dd")
                        database.child("users").child(auth.uid.toString()).child("Current").setValue("ss")
                            }

                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        button1.setOnClickListener {
            val intent = Intent(this@MainActivity3,MapsActivity2::class.java)
            startActivity(intent)
        }
        button2.setOnClickListener {
            val intent = Intent(this@MainActivity3,MainActivity5::class.java)
            startActivity(intent)
        }
        button3.setOnClickListener{
            auth.signOut()
            val intent = Intent(this@MainActivity3,MainActivity::class.java)
            startActivity(intent)

        }
        button4.setOnClickListener {
            val intent = Intent(this@MainActivity3,seller::class.java)
            startActivity(intent)
        }
        button5.setOnClickListener(){
            val intent = Intent(this@MainActivity3,Buyer::class.java)
            startActivity(intent)
        }
        button6.setOnClickListener(){
            val intent = Intent(this@MainActivity3,Current_Location::class.java)
            startActivity(intent)
        }
        button7.setOnClickListener(){
            val intent = Intent(this@MainActivity3,SuperMarketLocation::class.java)
            startActivity(intent)
        }
    }
}