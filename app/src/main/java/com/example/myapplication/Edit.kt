package com.example.myapplication

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage


class Edit : AppCompatActivity() {
    var count = 1
    private val mStorageRef = FirebaseStorage.getInstance().reference
    private var imageUri: Uri? = null
    private val pickImage = 100
    private lateinit var database: DatabaseReference
    lateinit var imageView: ImageView
    lateinit var button: Button
    lateinit var button1 : Button
    lateinit var  Name : EditText
    lateinit var  Price : EditText
    private lateinit var auth: FirebaseAuth
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        button=findViewById(R.id.get)
        button1=findViewById(R.id.addd)
        imageView = findViewById(R.id.image)
        Name = findViewById(R.id.Pname)
        Price = findViewById(R.id.Pprice)
        database = Firebase.database.reference
        auth = FirebaseAuth.getInstance()
       button1.setOnClickListener(){

    var x = intent.getSerializableExtra("key").toString()
        database.child("Drink").child((x)).child("name").setValue(Name.text.toString())
           database.child("Drink").child((x)).child("price").setValue(Price.text.toString())
           database.child("users").child(auth.uid.toString()).child("sell").child((x)).child("name").setValue(Name.text.toString()).addOnSuccessListener {
               System.out.println("ss")
           }
           database.child("users").child(auth.uid.toString()).child("sell").child(x).child("price").setValue(Price.text.toString())
           if(imageUri!=null){
           database.child("Drink")
               .addListenerForSingleValueEvent(object: ValueEventListener {
                   override fun onDataChange(snapshot: DataSnapshot) {
                       if(snapshot.exists())
                       {
                           for(drinkSnapshot in snapshot.children)
                           {
                               count++
                           }
                           val rnds = auth.uid + (0..1000).random()
                           val storage = mStorageRef.child("photos/"+rnds)
                           val uploadT =   storage.putFile(imageUri!!)
                           uploadT.addOnCompleteListener { task ->
                               if (task.isSuccessful) {
                                   storage.downloadUrl.addOnCompleteListener { task ->
                                       var key1 = "0"+(count.toString())
                                       database.child("Drink").child(x).child("image").setValue(task.getResult().toString())
                                       database.child("users").child(auth.uid.toString()).child("sell").child(x).child("image").setValue(task.getResult().toString())
                                   }

                               }
                           }




                       }
                   }
                   override fun onCancelled(error: DatabaseError) {

                   }
               })}
           val intent = Intent(this@Edit,MainActivity3::class.java)
           startActivity(intent)

       }
        button.setOnClickListener(){
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)

            startActivityForResult(gallery, pickImage)
        }

        FirebaseDatabase.getInstance()
            .getReference("users").child(auth.uid.toString()).child("sell")
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists())
                    {
                        for(drinkSnapshot in snapshot.children)
                        {
                            val intent = intent
                            var x = intent.getSerializableExtra("key").toString()
                            if(drinkSnapshot.key == (x)){
                                Name.setText(drinkSnapshot.child("name").getValue().toString())
                                Price.setText(drinkSnapshot.child("price").getValue().toString())
                                val imageUrl = drinkSnapshot.child("image").toString()
                                val imageUri = imageUrl.toUri().buildUpon().scheme("https").build()
                                System.out.println(imageUri)
                                Glide.with(imageView.context).load(imageUri).into(imageView)

                            }
                        }

                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })


    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)
        }


    }
}


