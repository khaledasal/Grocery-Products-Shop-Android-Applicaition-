package com.example.myapplication

import android.annotation.SuppressLint
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

class MyDrinkAdapter1(
    private val context: Context,
    private val list:List<DrinkModel>,
    private val cartListener: ICartLoadListener
) : RecyclerView.Adapter<MyDrinkAdapter1.MyDrinkViewHolder>(){
    private  var auth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var cartModel1 : CartModel
    private  var database: DatabaseReference =  Firebase.database.reference
    class MyDrinkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        var imageView:ImageView?=null
        var txtName:TextView?=null
        var txtPrice:TextView?=null

        private var clickListener:IRecyclerClickListener1? = null

        fun setClickListener(clickListener: IRecyclerClickListener1){
             this.clickListener = clickListener
        }



        init{
                imageView = itemView.findViewById(R.id.imageView) as ImageView
                txtName = itemView.findViewById(R.id.txtName) as TextView
                txtPrice = itemView.findViewById(R.id.txtPrice) as TextView

            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            clickListener!!.onItemClickListener(v,adapterPosition)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyDrinkViewHolder {
        return MyDrinkViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.layout_drink_item1,parent,false))
    }

    override fun onBindViewHolder(holder: MyDrinkViewHolder, @SuppressLint("RecyclerView") position: Int) {
        Glide.with(context)
            .load(list[position].image)
            .into(holder.imageView!!)
        holder.txtName!!.text = StringBuilder().append(list[position].name)
        holder.txtPrice!!.text = StringBuilder("$").append(list[position].price)

        holder.setClickListener(object:IRecyclerClickListener1{
            override fun onItemClickListener(view: View?, postion: Int) {
                addToCart(list[position])
                System.out.println("ff")
            }

        })
    }

    private fun addToCart(drinkModel: DrinkModel) {
        val userCart = FirebaseDatabase.getInstance()
            .getReference("Cart")
            .child("UNIQUE_USER_ID")

    userCart.child(auth.uid.toString() + drinkModel.key!!)
        .addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                {

                }
                else
                {
                    database.child("Drink").child(drinkModel.key!!)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {

                                    val cartModel = CartModel()
                                    cartModel.key = drinkModel.key
                                    cartModel.name = drinkModel.name
                                    cartModel.image = drinkModel.image
                                    cartModel.price = drinkModel.price
                                    cartModel.quntity = 1
                                    cartModel.itemid1 = auth.uid.toString()
                                    cartModel.totalPrice = drinkModel.price!!.toFloat()
                                    database.child("users").child(auth.uid.toString()).child("cart").child(snapshot.child("itemid").value.toString())
                                        .child(cartModel.key.toString()).setValue(cartModel)
                                    userCart.child(auth.uid.toString()+drinkModel.key!!)
                                        .setValue(cartModel)
                                        .addOnSuccessListener {
                                            EventBus.getDefault().postSticky(UpdateCartEvent())
                                            cartListener.onLoadCartFailed("success add to cart")
                                        }
                                        .addOnFailureListener{ e -> cartListener.onLoadCartFailed(e.message)}
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {

                            }
                        })

                }
            }

            override fun onCancelled(error: DatabaseError) {
                cartListener.onLoadCartFailed(error.message)
            }
        })



    }
    override fun getItemCount(): Int {
        return list.size
    }
}