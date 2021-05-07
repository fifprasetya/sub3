package com.example.gittalk.favorite

import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gittalk.DetailActivity
import com.example.gittalk.FavoriteModel
import com.example.gittalk.databinding.ActivityFavoriteBinding
import com.example.gittalk.db.FavoriteContract.FavColumns.Companion.CONTENT_URI
import com.example.gittalk.helper.MappingHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {
    private lateinit var adapter: FavoriteAdapter
    private lateinit var binding: ActivityFavoriteBinding

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
        private val TAG = FavoriteActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "ListFavorite"

        binding.rvUser.layoutManager = LinearLayoutManager(this)
        binding.rvUser.setHasFixedSize(true)
        adapter = FavoriteAdapter(this)
        binding.rvUser.adapter = adapter


        showLoading(true)

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler){
            override fun onChange(selfChange: Boolean) {
                loadFavoriteAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)
        if (savedInstanceState == null) {
            loadFavoriteAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<FavoriteModel>(EXTRA_STATE)
            if (list != null) {
                adapter.setData(list)
            }
        }

        adapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback{
            override fun onItemClicked(data: FavoriteModel) {
                Toast.makeText(this@FavoriteActivity, data.name, Toast.LENGTH_SHORT).show()
                val intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.USER_NAME, data.name)
                    intent.putExtra(DetailActivity.USER_ID, data.id)
                    val id = data.id
                    Log.d(TAG,"$id")
                    startActivity(intent)
                }
        })
    }




    private fun loadFavoriteAsync() {

        GlobalScope.launch(Dispatchers.Main) {
            showLoading(true)

            val defferedFav = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }

            showLoading(false)
            val favorites = defferedFav.await()
            if (favorites.size > 0) {
                adapter.setData(favorites)
            } else {
                adapter.setData(ArrayList<FavoriteModel>())
                showSnackbarMessage("Tidak ada data favorite saat ini")
            }

        }


    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFavorite)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(binding.rvUser, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        loadFavoriteAsync()
    }
}