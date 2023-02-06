package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class seller : AppCompatActivity(), IItemListener{
    lateinit var itemLoadListener: IItemListener
    lateinit var button1: Button
    lateinit var button2: Button
    private lateinit var database: DatabaseReference
    private lateinit var newRecyclerView: RecyclerView
    val fm:FragmentManager = supportFragmentManager
    val Items : MutableList<Item> = ArrayList()
    private lateinit var auth: FirebaseAuth
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller)
        auth = FirebaseAuth.getInstance()
       button1 = findViewById(R.id.aa)
        button2 =findViewById(R.id.back)
        database = Firebase.database.reference
        button2.setOnClickListener {
            val intent = Intent(this@seller,MainActivity3::class.java)
            startActivity(intent)
        }
        button1.setOnClickListener {
            database = Firebase.database.reference
            FirebaseDatabase.getInstance()
                .getReference("Location").child(auth.uid.toString())
                .addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(!snapshot.exists())
                        {
                            val intent = Intent(this@seller,SuperMarketLocation::class.java)
                            startActivity(intent)
                        }

                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            val intent = Intent(this@seller,Add::class.java)
            startActivity(intent)
          }
        init()
        loadDrinkFromFirebase()

        }
    fun loadDrinkFromFirebase() {

        FirebaseDatabase.getInstance()
            .getReference("users").child(auth.uid.toString()).child("sell")
            .addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists())
                    { for(drinkSnapshot in snapshot.children)
                    {       val drinkModel = drinkSnapshot.getValue(Item::class.java)
                            drinkModel!!.key = drinkSnapshot.key
                            Items.add(drinkModel)
                        }
                        itemLoadListener.onDrinkLoadSuccess(Items)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
    private fun init() {
        newRecyclerView = findViewById(R.id.recycler_drink)
        itemLoadListener = this
        val gridLayoutManager = GridLayoutManager(this,2)
        newRecyclerView.layoutManager = gridLayoutManager
        newRecyclerView.addItemDecoration(SpaceItemDecoration())
    }
    override fun onDrinkLoadSuccess(drinkModelList: List<Item>?) {
        val adapter = MyDrinkAdapter(this, drinkModelList!!)
        newRecyclerView.adapter = adapter

    }
    override fun onDrinkLoadFailed(message: String?) {
        }
}

