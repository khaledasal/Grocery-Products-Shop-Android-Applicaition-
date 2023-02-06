package com.example.myapplication



interface IDrinkLoadListener1 {
    fun onDrinkLoadSuccess(drinkModelList:List<DrinkModel>?)
    fun onDrinkLoadFailed(message:String?)
}