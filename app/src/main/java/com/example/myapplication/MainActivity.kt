package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    lateinit var button1: Button
    lateinit var button2 : Button
    lateinit var  email : EditText
    lateinit var  pass : EditText
    private lateinit var auth: FirebaseAuth
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button1 = findViewById(R.id.register)
        button2 = findViewById(R.id.startGameBtn)
        pass = findViewById(R.id.pass)
        email = findViewById(R.id.login)
        auth = FirebaseAuth.getInstance()
//        val intent = Intent(this@MainActivity,MapsActivity2::class.java)
        startActivity(intent)
        button1.setOnClickListener {
            val intent = Intent(this@MainActivity,MainActivity2::class.java)
            startActivity(intent)
        }
        button2.setOnClickListener {
        login()
        }
    }
    private fun login() {
        var email = email.text.toString()
        var pass = pass.text.toString()
        if(email.isEmpty() || pass.isEmpty()){
            Toast.makeText(applicationContext,"Please enter all the fields", Toast.LENGTH_SHORT).show()
        }
        else{
        auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val intent = Intent(this@MainActivity,MainActivity3::class.java)
                startActivity(intent)
            }
            else{

            }

        }}
    }


}
