package com.example.myapplication

data class User(val username: String? = null, val Age: String? = null, val Address: String? =null) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}