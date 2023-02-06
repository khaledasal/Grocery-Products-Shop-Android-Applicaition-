package com.example.myapplication

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.greenrobot.eventbus.EventBus



class MyCartAdapter(
    private val context: Context,
    private val cartModelList:List<CartModel>,
    private  var database: DatabaseReference = Firebase.database.reference,
            private var auth: FirebaseAuth = FirebaseAuth.getInstance()
): RecyclerView.Adapter<MyCartAdapter.MyCartViewHolder>() {


    class MyCartViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

        var btnMinus:ImageView?=null
        var btnPlus:ImageView?=null
        var imageView:ImageView?=null
        var btnDelete:ImageView?=null
        var txtName:TextView?=null
        var txtPrice: TextView?=null
        var txtQuantity:TextView?=null

        init{
            btnMinus = itemView.findViewById(R.id.btnMinus) as ImageView
            btnPlus = itemView.findViewById(R.id.btnPlus) as ImageView
            imageView = itemView.findViewById(R.id.imageView) as ImageView
            btnDelete = itemView.findViewById(R.id.btnDelete) as ImageView
            txtName = itemView.findViewById(R.id.txtName) as TextView
            txtPrice = itemView.findViewById(R.id.txtPrice) as TextView
            txtQuantity = itemView.findViewById(R.id.txtQuantity) as TextView
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCartViewHolder {
        return MyCartViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.layout_cart_item,parent,false))
    }

    override fun onBindViewHolder(holder: MyCartViewHolder, @SuppressLint("RecyclerView") position: Int) {
        Glide.with(context)
            .load(cartModelList[position].image)
            .into(holder.imageView!!)
        holder.txtName!!.text = StringBuilder().append(cartModelList[position].name)
        holder.txtPrice!!.text = StringBuilder("$").append(cartModelList[position].price)
        holder.txtQuantity!!.text = StringBuilder("").append(cartModelList[position].quntity)

        //Event
        holder.btnMinus!!.setOnClickListener{_ -> minusCartItem(holder,cartModelList[position]) }
        holder.btnPlus!!.setOnClickListener{_ -> plusCartItem(holder,cartModelList[position]) }
        holder.btnDelete!!.setOnClickListener{_ ->
            val dialog = AlertDialog.Builder(context)
                .setTitle("Delete Item")
                .setMessage("Do you really want to delete item")
                .setNegativeButton("CANCEL") {dialog,_ -> dialog.dismiss()}
                .setPositiveButton("DELETE") { dialog,_ ->
                    notifyItemRemoved(position)
                    System.out.println( database.child("Cart").child("UNIQUE_USER_ID").child(cartModelList[position].key.toString()))
                    database.child("Cart").child("UNIQUE_USER_ID").child(cartModelList[position].key.toString())
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    System.out.println(snapshot.child("key").toString())
                                    database.child("Drink").child(snapshot.child("key").value.toString())
                                        .addListenerForSingleValueEvent(object : ValueEventListener {
                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                if (snapshot.exists()) {
                                                    System.out.println(snapshot.child("itemid"))

                                                    database.child("users").child(auth.uid.toString()).child("cart").child(snapshot.child("itemid").value.toString())
                                                        .child(snapshot.child("key").value.toString()).removeValue().addOnSuccessListener{
                                                            database.child("Cart").child("UNIQUE_USER_ID")
                                                                .child(cartModelList[position].key!!)
                                                                .removeValue()
                                                                .addOnSuccessListener { EventBus.getDefault().postSticky(UpdateCartEvent())


                                                                }
                                                        }

                                                }
                                            }

                                            override fun onCancelled(error: DatabaseError) {

                                            }
                                        })
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {

                            }
                        })

                }

                .create()
            dialog.show()
        }

    }

    private fun plusCartItem(holder: MyCartViewHolder, cartModel: CartModel) {

        cartModel.quntity += 1
        cartModel.totalPrice = cartModel.quntity * cartModel.price!!.toFloat()
        holder.txtQuantity!!.text = StringBuilder("").append(cartModel.quntity)
        updateFirebase(cartModel)

    }

    private fun minusCartItem(holder: MyCartViewHolder, cartModel: CartModel) {
        if(cartModel.quntity > 1)
        {
            cartModel.quntity -= 1
            if(cartModel.quntity == 0){
                database.child("Cart").child("UNIQUE_USER_ID")
                    .child(cartModel.key!!)
                    .removeValue()
                    .addOnSuccessListener { EventBus.getDefault().postSticky(UpdateCartEvent()) }
            }else{
                cartModel.totalPrice = cartModel.quntity * cartModel.price!!.toFloat()
                holder.txtQuantity!!.text = StringBuilder("").append(cartModel.quntity)
                updateFirebase(cartModel)}

        }
    }

    private fun updateFirebase(cartModel: CartModel) {

        database.child("Cart").child("UNIQUE_USER_ID").child(cartModel.key.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        System.out.println(snapshot.child("key").toString())
                        database.child("Drink").child(snapshot.child("key").value.toString())
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.exists()) {
                                        System.out.println(snapshot.child("itemid1"))

                                        database.child("users").child(auth.uid.toString()).child("cart").child(snapshot.child("itemid").value.toString())
                                            .child(snapshot.child("key").value.toString()).child("quntity").setValue(cartModel.quntity)

                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {

                                }
                            })
                   }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

        FirebaseDatabase.getInstance()
            .getReference("Cart").child("UNIQUE_USER_ID")
            .child(cartModel.key!!).child("quntity")
            .setValue(cartModel.quntity)
            .addOnSuccessListener { EventBus.getDefault().postSticky(UpdateCartEvent())
            }
    //


    }

    override fun getItemCount(): Int {
        return cartModelList.size
    }


}