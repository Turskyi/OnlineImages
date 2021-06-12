package io.github.turskyi.onlineimages.ui.gallery

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import io.github.turskyi.onlineimages.R
import io.github.turskyi.onlineimages.data.entities.PhotoResponse
import io.github.turskyi.onlineimages.databinding.FragmentGalleryBinding

// We need to pass the layout into the constructor in order for to navigation will work
@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.fragment_gallery), OnItemClickListener {
    private val viewModel by viewModels<GalleryViewModel>()

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PhotoAdapter
    private lateinit var searchView: SearchView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)

        // init observers
        viewModel.photos.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    override fun onItemClick(photo: PhotoResponse) {
        val action: NavDirections = GalleryFragmentDirections
            .actionGalleryFragmentToDetailsFragment(photo)
        findNavController().navigate(action)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_gallery, menu)

        val searchItem: MenuItem = menu.findItem(R.id.action_search)
        if (searchItem.actionView is SearchView) {
            searchView = searchItem.actionView as SearchView
            initListeners(adapter)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initView(view: View) {
        _binding = FragmentGalleryBinding.bind(view)
        adapter = PhotoAdapter(this)
        binding.apply {
            recyclerView.setHasFixedSize(true)
            // getting rid of redundant flashes during changing the search query
            recyclerView.itemAnimator = null
            recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                header = PhotoLoadStateAdapter { adapter.retry() },
                footer = PhotoLoadStateAdapter { adapter.retry() },
            )
        }
        setHasOptionsMenu(true)
    }

    private fun initListeners(adapter: PhotoAdapter) {
        binding.buttonRetry.setOnClickListener { adapter.retry() }
        adapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
                textViewError.isVisible = loadState.source.refresh is LoadState.Error

                // controlling when empty view should be visible
                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount < 1
                ) {
                    recyclerView.isVisible = false
                    textViewEmpty.isVisible = true
                } else {
                    textViewEmpty.isVisible = false
                }
            }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                if (query != null) {
                    binding.recyclerView.scrollToPosition(0)
                    viewModel.searchPhotos(query)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }
}