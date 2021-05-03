package com.example.gittalk

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class MainViewModel : ViewModel() {

    val listUsers = MutableLiveData<ArrayList<UserItems>>()

    fun setUser(users: String?){
        val listItems = ArrayList<UserItems>()

        val client = AsyncHttpClient()
        val url = "https://api.github.com/search/users?q=$users"
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
                    val responseObject = JSONObject(result)
                    val items = responseObject.getJSONArray("items")

                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)
                        val userItems = UserItems()
                        userItems.id = item.getString("id")
                        userItems.name = item.getString("login")
                        userItems.avatar = item.getString("avatar_url")
                        userItems.repository = item.getString("repos_url")
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
                Toast.makeText(MainActivity(), errorMessage, Toast.LENGTH_SHORT).show()
                Log.d("onFailure", errorMessage)
            }

        })
    }

    fun getUser(): LiveData<ArrayList<UserItems>> {
        return listUsers
    }
}