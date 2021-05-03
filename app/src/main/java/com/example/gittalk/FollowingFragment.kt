package com.example.gittalk

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gittalk.databinding.FragmentFollowingBinding



class FollowingFragment : Fragment() {

    private lateinit var followingViewModel: FollowingViewModel
    companion object{
        const val EXTRA_USER = "extra_user"
        private val TAG = FollowingFragment::class.java.simpleName
    }
    fun newInstance(username:String):FollowingFragment{
        val fragment = FollowingFragment()
        val bundle = Bundle()
        bundle.putString(EXTRA_USER,username)
        fragment.arguments= bundle
        return fragment
    }
    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: UserAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFollowingBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(EXTRA_USER)
        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        binding.rvUser.layoutManager =LinearLayoutManager(activity)
        binding.rvUser.adapter = adapter
        followingViewModel = ViewModelProvider(this,ViewModelProvider.NewInstanceFactory()).get(FollowingViewModel::class.java)
        showLoading(true)
        Log.d(TAG, username.toString())
        followingViewModel.setUser(username)

        followingViewModel.getUser().observe(viewLifecycleOwner,{
            userItems->
            if (userItems !=null){
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