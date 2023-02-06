package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ActivityAddBinding


class MyDrinkAdapter(
    private val context: Context,
    private val list:List<Item>,

) : RecyclerView.Adapter<MyDrinkAdapter.MyDrinkViewHolder>(){

    class MyDrinkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var imageView:ImageView?=null
        var txtName:TextView?=null
        var txtPrice:TextView?=null
        private var clickListener :IRecyclerClickListener? = null
        fun setClickListner(clickListener: IRecyclerClickListener){
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
            .inflate(R.layout.layout_drink_item,parent,false))
    }

    override fun onBindViewHolder(holder: MyDrinkViewHolder, @SuppressLint("RecyclerView") position: Int) {
        Glide.with(context)
            .load(list[position].image)
            .into(holder.imageView!!)
        holder.txtName!!.text = StringBuilder().append(list[position].name)
        holder.txtPrice!!.text = StringBuilder("$").append(list[position].price)
        holder.setClickListner(object:IRecyclerClickListener{
            override fun onItemClickListener(view: View?, position: Int) {

                val intent = Intent(context,Edit::class.java)
                intent.putExtra("key", list.get(position).key)
                context.startActivity(intent)
            }

        })
    }

    private fun edit(position: Int) {

    }

    override fun getItemCount(): Int {
        return list.size
    }
}