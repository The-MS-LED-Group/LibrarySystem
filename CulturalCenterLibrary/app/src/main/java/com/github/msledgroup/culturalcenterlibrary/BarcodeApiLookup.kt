package com.github.msledgroup.culturalcenterlibrary

import android.util.Log
import kotlinx.coroutines.*
import org.json.JSONObject
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest

private val API_KEY = ""

class BarcodeApiLookup {
    fun ISBNapiCall(isbn: String): JSONObject? {
        var jsonRequest: JSONObject? = null
        val isbnURL: String = "https://www.googleapis.com/books/v1/volumes?q="
        val terms: String = "+isbn"
        val key: String = "&key=$API_KEY"
        CoroutineScope(Job() + Dispatchers.IO).launch {
            //request
            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, isbnURL + isbn + terms + key, null, {
                jsonRequest = it
            }, {
                Log.d("API Request Error", "${it.printStackTrace()}")
            })
        }
        return jsonRequest
    }
}