package com.example.fetchapp.fetch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fetchapp.R
import com.example.fetchapp.databinding.ActivityMainBinding
import com.example.fetchapp.fetch.api.ApiClient
import com.example.fetchapp.fetch.api.Item
import com.example.fetchapp.fetch.api.Service
import com.example.fetchapp.fetch.api.Status
import com.example.fetchapp.fetch.ui.ItemAdapter
import com.example.fetchapp.fetch.ui.StickyHeaderDecoration

class MainActivity : AppCompatActivity() {
    lateinit var mainBinding: ActivityMainBinding

    var itemList = ArrayList<Item>()

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var adapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainBinding = DataBindingUtil.setContentView(this,
            R.layout.activity_main
        )

        mainBinding.mainActivity = this

        setupViewModel()

        setupUI()

        setUpObserver()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiClient.apiClient().create(Service::class.java))
        ).get(MainActivityViewModel::class.java)
    }

    private fun setupUI() {
        mainBinding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        mainBinding.recyclerView.itemAnimator = DefaultItemAnimator()

        adapter = ItemAdapter(itemList)
        mainBinding.recyclerView.adapter = adapter

        mainBinding.recyclerView.addItemDecoration(
            StickyHeaderDecoration(
                adapter
            )
        )
        mainBinding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                this@MainActivity,
                LinearLayoutManager.VERTICAL
            )
        )
    }

    private fun setUpObserver() {
        viewModel.getData().observe(this, Observer {

            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        showProgress(false)
                        resource.data?.let { items -> retrieveFilteredList(items) }
                    }
                    Status.ERROR -> {
                        showProgress(false)
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                        Log.d("status_code", "" + it.data)
                    }
                    Status.LOADING -> {
                        showProgress(true)
                    }
                }
            }
        })
    }

    private fun retrieveFilteredList(items: List<Item>) {
        itemList.addAll(items)
        adapter.submitList(itemList)
        adapter.notifyDataSetChanged()
    }

    private fun showProgress(status: Boolean) {
        if (status) {
            mainBinding.showProgress.visibility = View.VISIBLE
        } else {
            mainBinding.showProgress.visibility = View.GONE
        }
    }
}