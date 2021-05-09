package io.github.turskyi.onlineimages.ui.gallery

import io.github.turskyi.onlineimages.data.api.PhotoResponse

interface OnItemClickListener {
    fun onItemClick(photo: PhotoResponse)
}