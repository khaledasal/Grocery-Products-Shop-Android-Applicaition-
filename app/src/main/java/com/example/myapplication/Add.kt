package com.example.myapplication

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream


class Add() : AppCompatActivity() {
    lateinit var byte: ByteArray
    lateinit var imageView: ImageView
    lateinit var button: Button
    lateinit var button1 : Button
    lateinit var  Name : EditText
    lateinit var  Price : EditText
    private val pickImage = 100
    private var imageUri: Uri? = null
    private val mStorageRef = FirebaseStorage.getInstance().reference
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    lateinit var button2 : Button

     var bytes : ByteArrayOutputStream? = null
    val im : ArrayList<String> = ArrayList()
    var count = 1
    private  val REQUEST_CODE = 42
    var check =1
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        button=findViewById(R.id.get)
        button1=findViewById(R.id.addd)
        imageView = findViewById(R.id.image)
        Name = findViewById(R.id.Pname)
        Price = findViewById(R.id.Pprice)
        database = Firebase.database.reference
        auth = FirebaseAuth.getInstance()
        button1.setOnClickListener {
            var name = Name.text.toString()
            var price = Price.text.toString()
            if((name.isEmpty() || price.isEmpty()) || imageUri.toString().isNullOrEmpty()){
                Toast.makeText(applicationContext,"Please enter all the fields", Toast.LENGTH_SHORT).show()
            }
            else{
                FirebaseDatabase.getInstance()
                    .getReference("Drink")
                    .addListenerForSingleValueEvent(object: ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(snapshot.exists())
                            {
                                for(drinkSnapshot in snapshot.children)
                                {
                                    count = drinkSnapshot.key.toString().toInt()+ 1
                                }


                                val rnds = auth.uid + (0..1000).random()
                                val storage = mStorageRef.child("photos/"+rnds)
                                val uploadT =   storage.putFile(imageUri!!)
                                uploadT.addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        storage.downloadUrl.addOnCompleteListener { task ->
                                            var key1 = "0"+(count.toString())
                                            val item1 = Item1(task.getResult().toString(),name,price,key1,auth.uid.toString())
                                            database.child("Drink").child("0"+(count.toString())).setValue(item1)
                                            database.child("users").child(auth.uid.toString()).child("sell").child("0"+(count.toString())).setValue(item1)
                                        }

                                    }
                                }




                            }
                        }
                        override fun onCancelled(error: DatabaseError) {

                        }
                    })
            }
            val intent = Intent(this@Add,MainActivity3::class.java)
            startActivity(intent)
        }
        button.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)
        }
        if(requestCode == REQUEST_CODE&& resultCode == Activity.RESULT_OK){
            val takeImage = data?.extras?.get("data")

            imageView.setImageBitmap(takeImage as Bitmap?)
            onCaptureImage(takeImage)
        }

    }

    private fun onCaptureImage(takeImage: Bitmap?) {

        if (takeImage != null) {
            takeImage.compress(Bitmap.CompressFormat.JPEG, 90,bytes)
            byte = bytes?.toByteArray()!!
            var s : String = String(byte)
            var uri : Uri = Uri.parse(s)
            System.out.println(uri)
            check = 0



        }
    }

}


