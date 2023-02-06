package com.example.myapplication



interface IItemListener {


    fun onDrinkLoadSuccess(drinkModelList:List<Item>?)
    fun onDrinkLoadFailed(message:String?)
}