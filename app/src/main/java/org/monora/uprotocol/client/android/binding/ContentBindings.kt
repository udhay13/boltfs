package org.monora.uprotocol.client.android.binding

import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import org.monora.uprotocol.client.android.GlideApp
import org.monora.uprotocol.client.android.R
import org.monora.uprotocol.client.android.database.model.UTransferItem
import org.monora.uprotocol.client.android.util.MimeIcons
import org.monora.uprotocol.client.android.viewmodel.content.FileContentViewModel
import org.monora.uprotocol.client.android.viewmodel.content.WebTransferContentViewModel
import org.monora.uprotocol.core.protocol.Direction
import org.monora.uprotocol.core.transfer.TransferItem.State.Done

@SuppressLint("CheckResult")
private fun load(imageView: ImageView, uri: Uri, circle: Boolean = false, @DrawableRes fallback: Int = 0) {
    GlideApp.with(imageView)
        .load(uri)
        .override(200)
        .also {
            if (fallback != 0) {
                it.fallback(fallback)
                    .error(fallback)
            }

            if (circle) {
                it.circleCrop()
            } else {
                it.centerCrop()
            }
        }
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(imageView)
}

@BindingAdapter("thumbnailOf")
fun loadThumbnailOf(imageView: ImageView, item: UTransferItem) {
    if (item.mimeType.startsWith("image/") || item.mimeType.startsWith("video/")
        && (item.direction == Direction.Outgoing || item.state == Done)
    ) {
        load(imageView, Uri.parse(item.location), circle = true)
    } else {
        imageView.setImageDrawable(null)
    }
}

@BindingAdapter("thumbnailOf")
fun loadThumbnailOf(imageView: ImageView, viewModel: FileContentViewModel) {
    if (viewModel.mimeType.startsWith("image/") || viewModel.mimeType.startsWith("video/")) {
        load(imageView, viewModel.uri, circle = true)
    } else {
        imageView.setImageDrawable(null)
    }
}

@BindingAdapter("thumbnailOf")
fun loadThumbnailOf(imageView: ImageView, viewModel: WebTransferContentViewModel) {
    if (viewModel.mimeType.startsWith("image/") || viewModel.mimeType.startsWith("video/")) {
        load(imageView, viewModel.uri, circle = true)
    } else {
        imageView.setImageDrawable(null)
    }
}

@BindingAdapter("thumbnailOf")
fun loadThumbnailOf(imageView: ImageView, info: ApplicationInfo) {
    GlideApp.with(imageView)
        .load(info)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(imageView)
}

@BindingAdapter("thumbnailOf")
fun loadThumbnailOf(imageView: ImageView, uri: Uri) {
    load(imageView, uri)
}

@BindingAdapter("albumArtOf")
fun loadAlbumArtOf(imageView: ImageView, uri: Uri) {
    load(imageView, uri, false, R.drawable.baseline_album_24)
}

@BindingAdapter("iconOf")
fun loadIconOf(imageView: ImageView, mimeType: String) {
    imageView.setImageResource(MimeIcons.loadMimeIcon(mimeType))
}
