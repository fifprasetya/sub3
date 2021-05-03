package com.example.gittalk

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import java.lang.Exception

class FollowerViewModel: ViewModel() {

    val listUsers = MutableLiveData<ArrayList<UserItems>>()

    fun setUser(users: String?){
        val listItems = ArrayList<UserItems>()

        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$users/followers"
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {

                try {
                    val result = String(responseBody)
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()) {

                        val jsonObject = jsonArray.getJSONObject(i)
                        val userItems = UserItems()
                        userItems.id = jsonObject.getString("id")
                        userItems.name = jsonObject.getString("login")
                        userItems.avatar = jsonObject.getString("avatar_url")
                        userItems.repository = jsonObject.getString("repos_url")
                        listItems.add(userItems)
                    }
                    listUsers.postValue(listItems)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {

                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Log.d("onFailure", errorMessage)
            }

        })
    }

    fun getUser(): LiveData<ArrayList<UserItems>>{
        return listUsers
    }
}