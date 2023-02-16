package org.monora.uprotocol.client.android.data

import android.provider.MediaStore.Audio.Media.ALBUM_ID
import android.provider.MediaStore.Audio.Media.IS_MUSIC
import org.monora.uprotocol.client.android.content.Album
import org.monora.uprotocol.client.android.content.AppStore
import org.monora.uprotocol.client.android.content.Artist
import org.monora.uprotocol.client.android.content.AudioStore
import org.monora.uprotocol.client.android.content.ImageBucket
import org.monora.uprotocol.client.android.content.ImageStore
import org.monora.uprotocol.client.android.content.VideoBucket
import org.monora.uprotocol.client.android.content.VideoStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaRepository @Inject constructor(
    private val appStore: AppStore,
    private val audioStore: AudioStore,
    private val imageStore: ImageStore,
    private val videoStore: VideoStore,
) {
    fun getAllAlbums() = audioStore.getAlbums()

    fun getAllArtists() = audioStore.getArtists()

    fun getAllApps() = appStore.getAll()

    fun getAllSongs() = audioStore.getSongs("$IS_MUSIC = ?", arrayOf("1"))

    fun getAlbumSongs(album: Album) = audioStore.getSongs("$ALBUM_ID = ?", arrayOf(album.id.toString()))

    fun getArtistAlbums(artist: Artist) = audioStore.getAlbums(artist)

    fun getImageBuckets() = imageStore.getBuckets()

    fun getImages(bucket: ImageBucket) = imageStore.getImages(bucket)

    fun getVideoBuckets() = videoStore.getBuckets()

    fun getVideos(bucket: VideoBucket) = videoStore.getVideos(bucket)
}
