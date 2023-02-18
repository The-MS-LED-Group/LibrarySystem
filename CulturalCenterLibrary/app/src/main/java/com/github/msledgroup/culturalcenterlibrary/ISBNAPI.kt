package com.github.msledgroup.culturalcenterlibrary

import android.util.Log
import kotlinx.coroutines.*
import org.json.JSONObject
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest



class ISBNAPI {
    fun apiCall(isbn: String): JSONObject? {
        var jsonRequest: JSONObject? = null
        val isbnURL: String = "https://openlibrary.org/isbn/"
        CoroutineScope(Job() + Dispatchers.IO).launch {
            //request
            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, isbnURL + isbn, null, {
                jsonRequest = it
            }, {
                Log.d("API Request Error", "${it.printStackTrace()}")
            })
        }
        return jsonRequest
    }
}