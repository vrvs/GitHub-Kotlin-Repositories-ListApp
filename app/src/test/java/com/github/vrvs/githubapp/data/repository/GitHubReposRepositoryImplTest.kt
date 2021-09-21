package com.github.vrvs.githubapp.data.repository

import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.github.vrvs.githubapp.Fixture.REPOSITORIES_ENTITY_LIST
import com.github.vrvs.githubapp.Fixture.REPOSITORIES_RESPONSE
import com.github.vrvs.githubapp.app.KoinTestApplication
import com.github.vrvs.githubapp.data.datasource.GitHubReposDataSource
import com.github.vrvs.githubapp.domain.entity.Result
import com.github.vrvs.githubapp.domain.repository.GitHubReposRepository
import kotlinx.coroutines.flow.collectIndexed
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
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

@RunWith(RobolectricTestRunner::class)
@Config(application = KoinTestApplication::class, sdk = [Build.VERSION_CODES.P])
class GitHubReposRepositoryImplTest : KoinTest {

    @Mock lateinit var gitHubReposRemoteDataSource: GitHubReposDataSource
    private lateinit var gitHubReposRepositoryImpl: GitHubReposRepository

    private lateinit var app: KoinTestApplication

    @Before
    fun setUp() {
        openMocks(this)
        app = ApplicationProvider.getApplicationContext()
        gitHubReposRepositoryImpl = GitHubReposRepositoryImpl()
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `git hub api should emit loading and then success`() =
        app.loadModules(
            module {
                single { gitHubReposRemoteDataSource }
            }
        ) {
            var count = 0
            runBlocking {
                whenever(gitHubReposRemoteDataSource.getRepositories(1)).thenReturn(REPOSITORIES_RESPONSE.items)
                val flow = gitHubReposRepositoryImpl.getRepositories(1)

                flow.collectIndexed { index, value ->
                    when (index) {
                        0 -> {
                            assertTrue {
                                value is Result.Loading
                            }
                            count+=2
                        }
                        1 -> {
                            assertEquals(
                                expected = REPOSITORIES_ENTITY_LIST,
                                actual = (value as Result.Success).data
                            )
                            count+=3
                            verify(gitHubReposRemoteDataSource).getRepositories(1)
                        }
                        else -> {
                            fail()
                        }
                    }
                }
            }
            assertEquals(5, count)
        }

    @Test
    fun `git hub api should emit loading and then error`() =
        app.loadModules(
            module {
                single { gitHubReposRemoteDataSource }
            }
        ) {
            var count = 0
            runBlocking {
                val error = Throwable()
                whenever(gitHubReposRemoteDataSource.getRepositories(1)).thenThrow(error)
                val flow = gitHubReposRepositoryImpl.getRepositories(1)

                flow.collectIndexed { index, value ->
                    when (index) {
                        0 -> {
                            assertTrue {
                                value is Result.Loading
                            }
                            count+=2
                        }
                        1 -> {
                            assertEquals(
                                expected = error,
                                actual = (value as Result.Error).error
                            )
                            count+=3
                            verify(gitHubReposRemoteDataSource).getRepositories(1)
                        }
                        else -> {
                            fail()
                        }
                    }
                }
            }
            assertEquals(5, count)
        }
}
