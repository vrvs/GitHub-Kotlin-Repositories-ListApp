package com.github.vrvs.githubapp.uicomponent.image

import android.content.Context
import android.util.AttributeSet
import com.bumptech.glide.Glide
import com.github.vrvs.githubapp.R
import com.github.vrvs.githubapp.utils.Utils
import com.github.vrvs.githubapp.utils.Utils.safeLet

class ImageComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr) {

    private var widthView: Int? = null
    private var heightView: Int? = null
    private var url: String? = null

    fun render(url: String?) {
        this.url = url
        loadUrl()
    }

    override fun onSizeChanged(
        w: Int,
        h: Int,
        oldw: Int,
        oldh: Int
    ) {
        super.onSizeChanged(w, h, oldw, oldh)
        widthView = w
        heightView = h
        loadUrl()
    }

    private fun loadUrl() = safeLet(widthView, heightView, url) { w, h, u ->
        Utils.processUrl(u).let {
            if (it.isNotEmpty()) {
                Glide
                    .with(context)
                    .load(url)
                    .centerCrop()
                    .placeholder(R.drawable.ic_outline_image_24)
                    .into(this)
            } else {
                Glide
                    .with(context)
                    .load(R.drawable.ic_outline_image_24)
                    .into(this)
            }
        }
    }
}