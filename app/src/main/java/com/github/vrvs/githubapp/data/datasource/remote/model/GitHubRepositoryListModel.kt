package com.github.vrvs.githubapp.data.datasource.remote.model

import com.google.gson.annotations.SerializedName

data class GitHubRepositoryListModel(
    @SerializedName("items")
    val items: List<GitHubRepositoryModel>,
)
