package io.github.turskyi.onlineimages.ui.gallery

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.turskyi.onlineimages.data.ImageRepository
import io.github.turskyi.onlineimages.data.entities.PhotoResponse
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val repository: ImageRepository,
    // handling process death
    @Assisted state: SavedStateHandle
) : ViewModel() {
    companion object {
        private const val DEFAULT_QUERY = "all"
        private const val CURRENT_QUERY = "current_query"
    }

    private val currentQuery: MutableLiveData<String> =
        state.getLiveData(CURRENT_QUERY, DEFAULT_QUERY)

    // "switchMap" allows us to swap data whenever "currentQuery.value" is changed
    val photos: LiveData<PagingData<PhotoResponse>> = currentQuery.switchMap { queryString ->
        // "cachedIn" Operator which caches a [LiveData] of [PagingData] within a [CoroutineScope].
        repository.getSearchResults(queryString).cachedIn(viewModelScope)
    }

    // changing the default value of query
    fun searchPhotos(query: String) {
        currentQuery.value = query
    }
}