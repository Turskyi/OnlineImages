package io.github.turskyi.onlineimages.data.api
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PhotoResponse(
    val id: String,
    val description: String?,
    val urls: PhotoUrls,
    val user: UnsplashUser
) : Parcelable {
// nested classes are added to the body of this data class because they belong to it logically
    @Parcelize
    data class PhotoUrls(
        val raw: String,
        val full: String,
        val regular: String,
        val small: String,
        val thumb: String,
    ) : Parcelable

    @Parcelize
    data class UnsplashUser(
        val name: String,
        val username: String
    ) : Parcelable {
/*        this attribute belongs to the body of this data class, because it is computed property,
* which means that it is generated dynamically from another (username) value ,
* and will be used for generating link to this particular "unsplash user" */
        val attributionUrl get() = "https://unsplash.com/$username?utm_source=OnlineImages&utm_medium=referral"
    }
}
