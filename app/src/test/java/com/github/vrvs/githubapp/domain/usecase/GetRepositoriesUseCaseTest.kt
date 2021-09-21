package com.github.vrvs.githubapp.domain.usecase

import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.github.vrvs.githubapp.Fixture.REPOSITORIES_ENTITY_LIST
import com.github.vrvs.githubapp.app.KoinTestApplication
import com.github.vrvs.githubapp.domain.entity.Result
import com.github.vrvs.githubapp.domain.repository.GitHubReposRepository
import com.github.vrvs.githubapp.domain.usecase.GetRepositoriesUseCase.Companion.PARAMETER_NOT_RECEIVED_MESSAGE_EXCEPTION
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyZeroInteractions
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

@RunWith(RobolectricTestRunner::class)
@Config(application = KoinTestApplication::class, sdk = [Build.VERSION_CODES.P])
class GetRepositoriesUseCaseTest : KoinTest {

    @Mock private lateinit var gitHubReposRepository: GitHubReposRepository

    private lateinit var app: KoinTestApplication
    private lateinit var useCase: GetRepositoriesUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        app = ApplicationProvider.getApplicationContext()
        useCase = GetRepositoriesUseCase()
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `get repositories use case should emit values that comes from repository when parameter exists`() =
        app.loadModules(
            module {
                single { gitHubReposRepository }
            }
        ) {
            var count = 0
            runBlocking {
                val error = Throwable()

                val flow = flowOf(
                    Result.Loading,
                    Result.Success(REPOSITORIES_ENTITY_LIST),
                    Result.Error(error),
                )
                whenever(gitHubReposRepository.getRepositories(1)).thenReturn(flow)

                useCase.parameter = 1
                useCase.execute().collectIndexed { index, value ->
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
                            verify(gitHubReposRepository).getRepositories(1)
                        }
                        2 ->  {
                            assertEquals(
                                expected = error,
                                actual = (value as Result.Error).error
                            )
                            count+=5
                            verify(gitHubReposRepository).getRepositories(1)
                        }
                        else -> {
                            fail()
                        }
                    }
                }
            }
            assertEquals(10, count)
        }

    @Test
    fun `get repositories use case should emit values that comes from repository when parameter is null`() =
        app.loadModules(
            module {
                single { gitHubReposRepository }
            }
        ) {
            var count = 0
            runBlocking {
                useCase.execute().collectIndexed { index, value ->
                    when (index) {
                        0 ->  {
                            assertEquals(
                                expected = PARAMETER_NOT_RECEIVED_MESSAGE_EXCEPTION,
                                actual = (value as Result.Error).error.message
                            )
                            count+=2
                            verifyZeroInteractions(gitHubReposRepository)
                        }
                        else -> {
                            fail()
                        }
                    }
                }
            }
            assertEquals(2, count)
        }
}
