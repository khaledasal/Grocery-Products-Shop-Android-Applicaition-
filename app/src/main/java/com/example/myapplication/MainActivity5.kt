package com.example.myapplication

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.io.ByteArrayOutputStream

class MainActivity5 : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var user: User
    lateinit var button2 : Button
    lateinit var  name : EditText
    lateinit var  age : EditText
    lateinit var  address : EditText
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main5)
        database = Firebase.database.reference
        name = findViewById(R.id.name)
        age = findViewById(R.id.age)
        address =findViewById(R.id.address)
        button2 =findViewById(R.id.register)
        auth = FirebaseAuth.getInstance()
        button2.setOnClickListener {
            var Name1 = name.text.toString()
            var Age1 = age.text.toString()
            var Address1 = address.text.toString()
            if(Name1.isEmpty() || Age1.isEmpty() || Address1.isEmpty() )
            {
                Toast.makeText(applicationContext,"Please enter all the fields", Toast.LENGTH_SHORT).show()
            }else{
            writeNewUser(auth.uid.toString(),Name1,Age1,Address1)}
        }
    }
    fun writeNewUser(userId: String, name: String, Age: String , Address : String) {
        val user = User(name, Age , Address)
        database.child("users").child(userId).setValue(user)
        database.child("users").child(userId).child("username").setValue(name)
        val intent = Intent(this@MainActivity5,MainActivity3::class.java)
        startActivity(intent)
    }
}

