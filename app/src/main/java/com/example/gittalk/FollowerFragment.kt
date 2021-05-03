package com.example.gittalk

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gittalk.databinding.FragmentFollowerBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception


class FollowerFragment : Fragment() {

    private var _binding: FragmentFollowerBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: UserAdapter
    private lateinit var followerViewModel: FollowerViewModel
    companion object {
        const val EXTRA_USER = "extra_user"
    }

    fun newInstance(username: String): FollowerFragment {
        val fragment = FollowerFragment()
        val bundle = Bundle()
        bundle.putString(EXTRA_USER, username)
        fragment.arguments = bundle
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFollowerBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = arguments?.getString(EXTRA_USER)
        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        binding.apply {
            rvUser.setHasFixedSize(true)
            rvUser.layoutManager = LinearLayoutManager(activity)
            rvUser.adapter = adapter
        }
        followerViewModel = ViewModelProvider(this,ViewModelProvider.NewInstanceFactory()).get(FollowerViewModel::class.java)
        showLoading(true)
        followerViewModel.setUser(username)

        followerViewModel.getUser().observe(viewLifecycleOwner,{
            userItems ->
            if(userItems != null){
                adapter.setData(userItems)
                showLoading(false)
            }
        })

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: UserItems) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(user: UserItems) {
        val intent = Intent(activity, DetailActivity::class.java)
        intent.putExtra(DetailActivity.USER_NAME,user.name)
        startActivity(intent)

    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}