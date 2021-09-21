package com.github.vrvs.githubapp.uicomponent.repository

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.github.vrvs.githubapp.R
import com.github.vrvs.githubapp.uicomponent.image.ImageComponent
import com.github.vrvs.githubapp.uicomponent.image.SquareRoundedImageComponent

class RepositoryComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): LinearLayout(context, attrs, defStyleAttr) {

    data class ComponentModel(
        val id: String,
        val repoName: String,
        val authorName: String,
        val starsNumber: Int,
        val forksNumber: Int,
        val imageUrl: String?
    )

    private val repoName: TextView
        get() = findViewById(R.id.repo_name)
    private val authorName: TextView
        get() = findViewById(R.id.author_name)
    private val startsNumber: TextView
        get() = findViewById(R.id.stars_number)
    private val forksNumber: TextView
        get() = findViewById(R.id.forks_number)
    private val imageComponent: SquareRoundedImageComponent
        get() = findViewById(R.id.author_image)

    init {
        LayoutInflater.from(context).inflate(R.layout.component_repository, this, true)
    }

    fun render(
        componentModel: ComponentModel
    ) {
        repoName.text = componentModel.repoName
        authorName.text = componentModel.authorName
        startsNumber.text = componentModel.starsNumber.toString()
        forksNumber.text = componentModel.forksNumber.toString()
        imageComponent.render(componentModel.imageUrl)
    }
}