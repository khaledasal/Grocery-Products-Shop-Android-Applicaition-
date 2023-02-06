package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity2 : AppCompatActivity() {
    //private lateinit var binding:

    private lateinit var editText2 : EditText
    private lateinit var editText3 : EditText
    lateinit var button1 : Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        editText2 = findViewById(R.id.email)
        editText3 = findViewById(R.id.pass)
        button1 = findViewById(R.id.register)
        auth = FirebaseAuth.getInstance()


        button1.setOnClickListener {
            PerformAuth()
        }

    }
    private fun PerformAuth() {

        var email = editText2.text.toString()
        var pass = editText3.text.toString()
        if(email.isEmpty() || pass.isEmpty()){
            Toast.makeText(applicationContext,"Please enter all the fields",Toast.LENGTH_SHORT).show()
        }
        else{
        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener { task ->
            if (task.isSuccessful) {

                Toast.makeText(applicationContext,"Signed up Successfully",Toast.LENGTH_SHORT).show()
                val intent = Intent(this@MainActivity2,MainActivity::class.java)
                startActivity(intent)

            }
            else{
                Toast.makeText(applicationContext,"Please Signed up again",Toast.LENGTH_SHORT).show()
            }

        }}




    }

}