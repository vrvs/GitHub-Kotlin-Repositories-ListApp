package com.github.vrvs.githubapp.presentation.viewmodel

import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.github.vrvs.githubapp.Fixture.REPOSITORIES_COMPONENT_MODEL_LIST
import com.github.vrvs.githubapp.Fixture.REPOSITORIES_ENTITY_LIST
import com.github.vrvs.githubapp.app.KoinTestApplication
import com.github.vrvs.githubapp.domain.entity.GitHubRepositoryEntity
import com.github.vrvs.githubapp.domain.entity.Result
import com.github.vrvs.githubapp.domain.repository.GitHubReposRepository
import com.github.vrvs.githubapp.presentation.contract.GitHubReposContract.Intent
import com.github.vrvs.githubapp.presentation.contract.GitHubReposContract.SideEffect
import com.github.vrvs.githubapp.presentation.contract.GitHubReposContract.State
import com.github.vrvs.githubapp.presentation.viewmodel.GitHubReposViewModel.Companion.TOAST_TEXT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
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

@RunWith(RobolectricTestRunner::class)
@Config(application = KoinTestApplication::class, sdk = [Build.VERSION_CODES.P])
class GitHubReposViewModelTest : KoinTest {

    @Mock private lateinit var gitHubReposRepository: GitHubReposRepository

    private lateinit var app: KoinTestApplication

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        app = ApplicationProvider.getApplicationContext()
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `state should send success when uses cases send it`() =
        app.loadModules(
            module {
                single { gitHubReposRepository }
            }
        ) {
            var cont = 0
            runBlocking(Dispatchers.IO) {
                val gitHubReposViewModel = GitHubReposViewModel()
                gitHubReposViewModel.page.set(2)

                assertTrue {
                    gitHubReposViewModel.state.value is State.Loading
                }

                val flow: Flow<Result<List<GitHubRepositoryEntity>>> = flowOf(
                    Result.Success(REPOSITORIES_ENTITY_LIST)
                )

                whenever(gitHubReposRepository.getRepositories(2)).thenReturn(flow)
                var scope: Job? = null
                scope = this.launch {
                    gitHubReposViewModel.state.collectIndexed { index, value ->
                        when (index) {
                            0 -> {
                                assertEquals(
                                    State.Loading,
                                    value,
                                )
                                cont += 2
                                gitHubReposViewModel.interpreter.emit(Intent.LoadMore)
                            }
                            1 -> {
                                assertEquals(
                                    value,
                                    State.UpdateList(REPOSITORIES_COMPONENT_MODEL_LIST)
                                )
                                verify(gitHubReposRepository).getRepositories(2)
                                assertEquals(3, gitHubReposViewModel.page.get())
                                cont+=3

                                scope?.cancel()
                            }
                        }
                    }
                }
            }
            assertEquals(5, cont)
        }

    @Test
    fun `state should send error when uses cases send it`() =
        app.loadModules(
            module {
                single { gitHubReposRepository }
            }
        ) {
            var cont = 0
            runBlocking(Dispatchers.IO) {
                val gitHubReposViewModel = GitHubReposViewModel()
                val error = Throwable()

                gitHubReposViewModel.page.set(2)

                assertTrue {
                    gitHubReposViewModel.state.value is State.Loading
                }

                val flow: Flow<Result<List<GitHubRepositoryEntity>>> = flowOf(
                    Result.Error(error),
                )

                whenever(gitHubReposRepository.getRepositories(2)).thenReturn(flow)
                var scope: Job? = null
                scope = this.launch {
                    gitHubReposViewModel.state.collectIndexed { index, value ->
                        when (index) {
                            0 -> {
                                assertEquals(
                                    State.Loading,
                                    value,
                                )
                                cont += 2
                                gitHubReposViewModel.interpreter.emit(Intent.LoadMore)
                            }
                            1 -> {
                                assertEquals(
                                    value,
                                    State.Error(error)
                                )
                                verify(gitHubReposRepository).getRepositories(2)
                                assertEquals(2, gitHubReposViewModel.page.get())
                                cont+=3
                                scope?.cancel()
                            }
                        }
                    }
                }
            }
            assertEquals(5, cont)
        }

    @Test
    fun `state should send success when uses cases send it - Reload`() =
        app.loadModules(
            module {
                single { gitHubReposRepository }
            }
        ) {
            var cont = 0
            runBlocking(Dispatchers.IO) {
                val gitHubReposViewModel = GitHubReposViewModel()
                gitHubReposViewModel.page.set(2)

                assertTrue {
                    gitHubReposViewModel.state.value is State.Loading
                }

                val flow: Flow<Result<List<GitHubRepositoryEntity>>> = flowOf(
                    Result.Success(REPOSITORIES_ENTITY_LIST)
                )

                whenever(gitHubReposRepository.getRepositories(1)).thenReturn(flow)
                var scope: Job? = null
                scope = this.launch {
                    gitHubReposViewModel.state.collectIndexed { index, value ->
                        when (index) {
                            0 -> {
                                assertEquals(
                                    State.Loading,
                                    value,
                                )
                                cont += 2
                                gitHubReposViewModel.interpreter.emit(Intent.Reload)
                            }
                            1 -> {
                                assertEquals(
                                    State.UpdateList(REPOSITORIES_COMPONENT_MODEL_LIST, false),
                                    value,
                                )
                                verify(gitHubReposRepository).getRepositories(1)
                                assertEquals(2, gitHubReposViewModel.page.get())
                                cont+=3

                                scope?.cancel()
                            }
                        }
                    }
                }
            }
            assertEquals(5, cont)
        }

    @Test
    fun `side effect should send show toast`() =
        app.loadModules(
            module {
                single { gitHubReposRepository }
            }
        ) {
            var cont = 0
            runBlocking(Dispatchers.IO) {
                val gitHubReposViewModel = GitHubReposViewModel()
                val id = "id"
                val id2 = "id2"

                gitHubReposViewModel.interpreter.emit(Intent.ClickItem(id))
                var scope: Job? = null
                scope = this.launch {
                    gitHubReposViewModel.sideEffect.collectIndexed { index, value ->
                        when (index) {
                            0 -> {
                                assertEquals(
                                    SideEffect.ShowToast(TOAST_TEXT+id),
                                    value,
                                )
                                cont+=2
                                gitHubReposViewModel.interpreter.emit(Intent.ClickItem(id2))
                            }
                            1 -> {
                                assertEquals(
                                    SideEffect.ShowToast(TOAST_TEXT+id2),
                                    value,
                                )
                                verifyZeroInteractions(gitHubReposRepository)
                                cont+=3

                                scope?.cancel()
                            }
                        }
                    }
                }
            }
            assertEquals(5, cont)
        }
}