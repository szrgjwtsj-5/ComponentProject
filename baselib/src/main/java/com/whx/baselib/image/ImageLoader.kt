package com.whx.baselib.image

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

object ImageLoader {

    @SuppressLint("CheckResult")
    fun load(
        resource: Any?,
        target: ImageView, @DrawableRes placeHolder: Int = 0,
        listener: RequestListener<Drawable>? = null,
        transformation: BitmapTransformation? = null,
        diskCacheStrategy: DiskCacheStrategy? = null,
        decodeFormat: DecodeFormat? = null,
        thumbSize: Float = 0f
    ): Target<Drawable>? {
        val act = getActivityFromView(target.context)

        val requestManager = if (isActivityValid(act)) {
            Glide.with(act!!)
        } else {
            Glide.with(target.context)
        }

        val options = RequestOptions()
        if (diskCacheStrategy != null) {
            options.diskCacheStrategy(diskCacheStrategy)
        }
        if (decodeFormat != null) {
            options.format(decodeFormat)
        }
        if (transformation == null) {
            options.dontTransform()
        } else {
            options.transform(transformation)
        }

        val requestBuilder = requestManager.load(resource).apply(options)
            .addListener(listener)
            .placeholder(placeHolder)

        if (thumbSize != 0.0f) {
            requestBuilder.thumbnail(thumbSize)
        }

        return requestBuilder.into(target)
    }

    private fun isActivityValid(activity: Activity?): Boolean {
        return activity != null && !activity.isFinishing && !activity.isDestroyed
    }

    private fun getActivityFromView(context: Context?): Activity? {
        if (context == null) return null

        return when (context) {
            is Activity -> context
            is ContextWrapper -> getActivityFromView(context.baseContext)
            else -> null
        }
    }
}