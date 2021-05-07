package com.example.gittalk

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gittalk.databinding.ActivityMainBinding
import com.example.gittalk.favorite.FavoriteActivity

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: UserAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = " "
        adapter = UserAdapter()
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        binding.rvUser.adapter = adapter


        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)


        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserItems) {
                showSelectedUser(data)
            }
        })

    }
    private fun showSelectedUser(user: UserItems) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.USER_NAME,user.name)
        intent.putExtra(DetailActivity.USER_ID, user.id)
        startActivity(intent)

    }


    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                var user = query.toString()
                Toast.makeText(this@MainActivity, user, Toast.LENGTH_SHORT).show()
                showLoading(true)

                mainViewModel.setUser(user)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        mainViewModel.getUser().observe(this,{
            userItems ->
            if (userItems != null){
                adapter.setData(userItems)
                showLoading(false)
            }
        })
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu2 -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
                return true
            }
            R.id.but_fav -> {
                val mIntent = Intent(this, FavoriteActivity::class.java)
                startActivity(mIntent)
                return true
            }
            R.id.menu_alarm ->{
                val mIntent = Intent(this,SettingActivity::class.java)
                startActivity(mIntent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}