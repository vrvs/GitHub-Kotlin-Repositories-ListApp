package com.github.vrvs.githubapp.uicomponent.image

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.github.vrvs.githubapp.R

class SquareRoundedImageComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): FrameLayout(context, attrs, defStyleAttr) {

    private val imageComponent: ImageComponent
        get() = findViewById(R.id.icon)

    init {
        LayoutInflater.from(context).inflate(R.layout.component_square_rounded_image, this, true)
    }

    fun render(url: String?) {
        imageComponent.render(url)
    }
}