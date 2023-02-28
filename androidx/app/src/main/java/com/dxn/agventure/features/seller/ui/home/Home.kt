package com.dxn.agventure.features.seller.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.GridLayout.VERTICAL
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dxn.agventure.R
import com.dxn.agventure.databinding.HomeFragmentBinding
import com.dxn.agventure.features.auth.AuthActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject


@AndroidEntryPoint
class Home : Fragment() {
    private val viewModel by hiltNavGraphViewModels<HomeViewModel>(R.id.seller_navigation)

    @Inject
    lateinit var auth: FirebaseAuth

    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        initUi()
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_add)
        }
        return binding.root
    }

    private fun initUi() {
        val productListAdapter = ProductListAdapter { viewModel.removeProduct(it) }
        binding.productsRecycler.adapter = productListAdapter
        binding.productsRecycler.layoutManager =
            GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
        binding.signOut.setOnClickListener {
            auth.signOut()
            startActivity(Intent(requireContext(), AuthActivity::class.java))
        }

        lifecycleScope.launchWhenCreated {
            viewModel.products.collect { uiState ->
                when (uiState) {
                    is HomeViewModel.HomeUiState.Success -> {
                        productListAdapter.products = uiState.products
                        productListAdapter.notifyDataSetChanged()
                    }
                    is HomeViewModel.HomeUiState.Error -> {
                        showErrorScreen()
                    }
                    is HomeViewModel.HomeUiState.Loading -> {
                        showLoadingScreen()
                    }
                }
            }
        }
    }

    private fun showErrorScreen() {

    }

    private fun showLoadingScreen() {

    }

    companion object {
        const val TAG = "HOME_FRAGMENT"
    }

}