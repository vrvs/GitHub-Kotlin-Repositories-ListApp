package com.github.vrvs.githubapp.data.datasource.remote

import com.github.vrvs.githubapp.Fixture.REPOSITORIES_RESPONSE
import com.github.vrvs.githubapp.Utils.readJsonFile
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.test.assertEquals

class GitHubApiTest {

    private lateinit var server: MockWebServer
    private lateinit var gitHubApi: GitHubApi

    @Before
    fun setUp() {
        server = MockWebServer()

        val retrofit = Retrofit.Builder()
            .baseUrl(server.url(""))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        gitHubApi = retrofit.create(GitHubApi::class.java)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `git hub api should call correct path and return expected response`() {
        val json = readJsonFile("repositories_200.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(json))
        assertEquals(
            expected = REPOSITORIES_RESPONSE,
            actual = gitHubApi.getRepositoriesAsync(
                query = "language:kotlin",
                sort = "stars",
                perPage = 2,
                page = 1,
            ).execute().body()
        )
    }
}