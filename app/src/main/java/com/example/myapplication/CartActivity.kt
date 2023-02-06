package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_main1.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class CartActivity : AppCompatActivity(), ICartLoadListener {
    var cartLoadListener : ICartLoadListener?= null
    private  var auth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)

    }

    override fun onStop(){
        super.onStop()
        if(EventBus.getDefault().hasSubscriberForEvent(UpdateCartEvent::class.java))
            EventBus.getDefault().removeStickyEvent(UpdateCartEvent::class.java)
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public fun onUpdateCartEvent(event: UpdateCartEvent)
    {
        loadCartFirebase()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        auth = FirebaseAuth.getInstance()
        init()
        loadCartFirebase()
    }

    private fun loadCartFirebase() {
        val cartModels : MutableList<CartModel> = ArrayList()
        FirebaseDatabase.getInstance()
            .getReference("Cart")
            .child("UNIQUE_USER_ID")
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(cartSnapshot in snapshot.children)
                    { if(cartSnapshot.child("itemid1").getValue().toString().equals(auth.uid.toString())){
                        val cartModel = cartSnapshot.getValue(CartModel::class.java)
                        cartModel!!.key = cartSnapshot.key
                        cartModels.add(cartModel)}
                    }
                    cartLoadListener!!.onLoadCartSuccess(cartModels)
                }

                override fun onCancelled(error: DatabaseError) {
                    cartLoadListener!!.onLoadCartFailed(error.message)
                }

            })
    }

    private fun init() {
        cartLoadListener = this
        val layoutManager = LinearLayoutManager(this)
        recycler_cart!!.layoutManager = layoutManager
        Check.setOnClickListener{(startActivity(Intent(this,Check_Out::class.java)))}
        recycler_cart!!.addItemDecoration(DividerItemDecoration(this,layoutManager.orientation))
        btnBack!!.setOnClickListener{ finish() }


    }
    override fun onLoadCartSuccess(cartModeList: List<CartModel>) {
        var sum = 0.0
        for(cartModel in cartModeList!!){
            sum += cartModel!!.price?.toDouble()!! * cartModel!!.quntity
        }
        txtTotal.text =StringBuilder("$").append(sum)
        val adapter = MyCartAdapter(this,cartModeList)
        recycler_cart!!.adapter = adapter
    }

    override fun onLoadCartFailed(message: String?) {
        Snackbar.make(mainLayout,message!!,Snackbar.LENGTH_LONG).show()
    }
}