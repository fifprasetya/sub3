package com.example.gittalk

import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.gittalk.databinding.ActivityDetailBinding
import com.example.gittalk.db.FavoriteContract.FavColumns.Companion.AVATAR
import com.example.gittalk.db.FavoriteContract.FavColumns.Companion.CONTENT_URI
import com.example.gittalk.db.FavoriteContract.FavColumns.Companion.NAME
import com.example.gittalk.db.FavoriteContract.FavColumns.Companion.REPOSITORY
import com.example.gittalk.db.FavoriteContract.FavColumns.Companion._ID
import com.example.gittalk.db.FavoriteHelper
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONObject

class DetailActivity : AppCompatActivity() {
    private lateinit var profileCircleImageView: CircleImageView
    private lateinit var favoriteHelper: FavoriteHelper
    private var statusFavorites: Boolean = false
    var nameUser: String? = null
    var idUser: String = ""
    var avatarUser: String? = null
    var repositoryUser: String? = null
    private lateinit var uriWithId: Uri

    companion object {
        const val USER_NAME = "username"
        const val USER_ID = "id"
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_follower,
            R.string.tab_text_following
        )

        private val TAG = DetailActivity::class.java.simpleName

    }


    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading(true)
        favoriteHelper = FavoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()


        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val username = intent.getStringExtra(USER_NAME)
        sectionsPagerAdapter.username = username
        val tabs: TabLayout = findViewById(R.id.tabslayout)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()


        getDetail()
        uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + idUser)
        val userId : String = intent.getStringExtra(USER_ID)
        Log.d(TAG,"$userId")
        Log.d(TAG, "Mencari Data")
        val cursor: Cursor = favoriteHelper.queryById(userId)
        if (cursor.moveToNext()) {
            statusFavorites = true
            setStatusFavorite(true)
            Log.d(TAG, " $userId Data ditemukan")
        } else {
            Log.d(TAG, " $userId Data tidak ditemukan")
        }
        Log.d(TAG, "Mencari Data Selesai")


        binding.toggleFav.setOnCheckedChangeListener { buttonView, isChecked ->
            statusFavorites = isChecked
            //statusFavorite = !statusFavorite
            if (statusFavorites) {
                val values = ContentValues()
                values.put(_ID, idUser)
                values.put(NAME, nameUser)
                values.put(AVATAR, avatarUser)
                values.put(REPOSITORY, repositoryUser)
                //contentResolver.insert(uriWithId, values)
                favoriteHelper.insert(values)
                Toast.makeText(this, "Favorite ditambahkan", Toast.LENGTH_SHORT).show()
                //setStatusFavorite(statusFavorite)
            } else {
                //contentResolver.delete(uriWithId,null,null)
                favoriteHelper.deleteById(idUser)
                Toast.makeText(this, "$idUser dihapus", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setStatusFavorite(statusFavorite: Boolean) {
        if (statusFavorite) {
            binding.toggleFav.isChecked = true
        } else {
            binding.toggleFav.isChecked = false
            return
        }
    }


    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun getDetail() {
        showLoading(true)
        val username = intent.getStringExtra(USER_NAME)

        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username"
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                showLoading(false)
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)

                    binding.tvItemUsername.text = username
                    binding.tvItemId.text = responseObject.getString("id")
                    binding.tvItemFullname.text = responseObject.getString("name")
                    binding.tvItemCompany.text = responseObject.getString("company")
                    binding.tvItemLocation.text = responseObject.getString("location")
                    binding.tvItemRepository.text = responseObject.getString("public_repos")
                    binding.tvItemFollowers.text = responseObject.getString("followers")
                    binding.tvItemFollowing.text = responseObject.getString("following")

                    nameUser = username
                    idUser = responseObject.getString("id")
                    avatarUser = responseObject.getString("avatar_url")
                    repositoryUser = responseObject.getString("public_repos")
        /*
                    Log.d(TAG, "Mencari Data")
                    val cursor: Cursor = favoriteHelper.queryById(idUser)
                    if (cursor.moveToNext()){
                        statusFavorites = true
                        setStatusFavorite(true)
                        Log.d(TAG," $idUser Data ditemukan" )
                    }else{
                        Log.d(TAG, " $idUser Data tidak ditemukan")
                    }
                    Log.d(TAG, "Mencari Data Selesai")
                    */
                    var profileImageUrl = responseObject.getString("avatar_url")
                    profileCircleImageView = binding.imgItemPhoto

                    Glide.with(this@DetailActivity)
                        .load(profileImageUrl)
                        .apply(RequestOptions().override(350, 350))
                        .into(profileCircleImageView)
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
                binding.progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@DetailActivity, errorMessage, Toast.LENGTH_SHORT).show()
                Log.d("onFailure", error.message.toString())
            }

        })

    }
}