package com.github.vrvs.githubapp.data.datasource.remote

import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.github.vrvs.githubapp.Fixture.REPOSITORIES_RESPONSE
import com.github.vrvs.githubapp.app.KoinTestApplication
import com.github.vrvs.githubapp.data.datasource.remote.model.GitHubRepositoryListModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.mockito.Mock
import org.mockito.MockitoAnnotations.openMocks
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
@Config(application = KoinTestApplication::class, sdk = [Build.VERSION_CODES.P])
class GitHubReposRemoteDataSourceTest: KoinTest {

    @Mock lateinit var gitHubApiMock: GitHubApi
    @Mock lateinit var mockCall: Call<GitHubRepositoryListModel>
    private lateinit var gitHubReposRemoteDataSource: GitHubReposRemoteDataSource

    private lateinit var app: KoinTestApplication

    @Before
    fun setUp() {
        openMocks(this)
        app = ApplicationProvider.getApplicationContext()

        gitHubReposRemoteDataSource = GitHubReposRemoteDataSource()
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `git hub data source should and return expected response`() =
        app.loadModules(
            module {
                single { gitHubApiMock }
            }
        ) {
            runBlocking(Dispatchers.IO) {
                whenever(gitHubApiMock.getRepositoriesAsync(any(), any(), any(), any())).thenReturn(
                    mockCall
                )

                whenever(mockCall.enqueue(any())).then {
                    it.getArgument<Callback<GitHubRepositoryListModel>>(0).onResponse(
                        mockCall,
                        Response.success(REPOSITORIES_RESPONSE)
                    )
                }

                assertEquals(
                    expected = REPOSITORIES_RESPONSE.items,
                    actual = gitHubReposRemoteDataSource.getRepositories(1)
                )
            }
        }
}