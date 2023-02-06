package com.example.myapplication




interface ICartLoadListener {
    fun onLoadCartSuccess(cartModeList: List<CartModel>)
    fun onLoadCartFailed(message:String?)
}