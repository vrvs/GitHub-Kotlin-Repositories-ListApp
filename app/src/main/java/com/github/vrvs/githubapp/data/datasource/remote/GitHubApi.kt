package com.github.vrvs.githubapp.data.datasource.remote

import com.github.vrvs.githubapp.data.datasource.remote.model.GitHubRepositoryListModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface GitHubApi {

    @Headers("Accept: application/vnd.github.v3+json")
    @GET("search/repositories")
    fun getRepositoriesAsync(
        @Query("q") query: String,
        @Query("sort") sort: String,
        @Query("per_page") perPage: Long,
        @Query("page") page: Long,
    ) : Call<GitHubRepositoryListModel>


    companion object {
        const val URL = "https://api.github.com/"
    }
}