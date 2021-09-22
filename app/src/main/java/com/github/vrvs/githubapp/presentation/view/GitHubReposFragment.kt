package com.github.vrvs.githubapp.presentation.view

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.github.vrvs.githubapp.R
import com.github.vrvs.githubapp.presentation.contract.GitHubReposContract.*
import com.github.vrvs.githubapp.presentation.viewmodel.GitHubReposViewModel
import com.github.vrvs.githubapp.uicomponent.error.ErrorComponent
import com.github.vrvs.githubapp.uicomponent.loading.LoadingComponent
import com.github.vrvs.githubapp.uicomponent.repository.RepositoryListComponent
import com.github.vrvs.githubapp.uicomponent.repository.RepositoryListComponent.Action
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent

class GitHubReposFragment: Fragment(R.layout.fragment_git_hub_repos), KoinComponent {

    private val viewModel: GitHubReposViewModel by viewModel()
    private lateinit var listComponent: RepositoryListComponent

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        listComponent = view.findViewById(R.id.repo_list_component)
        val errorComponent = view.findViewById<ErrorComponent>(R.id.error_component)
        val loadingComponent = view.findViewById<LoadingComponent>(R.id.loading_component)
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                when (state) {
                    is State.UpdateList -> {
                        if (state.append) {
                            listComponent.appendDataSet(state.list)
                        } else {
                            listComponent.changeDataSet(state.list)
                        }
                        errorComponent.visibility = GONE
                        loadingComponent.visibility = GONE
                    }
                    is State.Loading -> {
                        errorComponent.visibility = GONE
                        loadingComponent.visibility = VISIBLE
                    }
                    is State.Error -> {
                        errorComponent.visibility = VISIBLE
                        loadingComponent.visibility = GONE
                        listComponent.error()
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.sideEffect.collect { sideEffect ->
                when (sideEffect) {
                    is SideEffect.ShowToast -> {
                        Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


        listComponent.output.observe(viewLifecycleOwner) { action ->
            lifecycleScope.launch {
                viewModel.interpreter.emit(
                    when (action) {
                        is Action.ItemClicked -> {
                            Intent.ClickItem(action.id)
                        }
                        is Action.LoadMoreData -> {
                            Intent.LoadMore
                        }
                        is Action.Refresh -> {
                            Intent.Reload
                        }
                    }
                )
            }
        }

        errorComponent.buttonClicked.observe(viewLifecycleOwner) { button ->
            when (button) {
                ErrorComponent.Button.CLOSE -> {
                    errorComponent.visibility = GONE
                    lifecycleScope.launch {
                        viewModel.interpreter.emit(Intent.SaveState(listComponent.getList(), true))
                    }
                }
                ErrorComponent.Button.TRY_AGAIN -> {
                    lifecycleScope.launch {
                        viewModel.interpreter.emit(Intent.Reload)
                    }
                }
            }
        }
    }

    override fun onStop() {
        lifecycleScope.launch {
            viewModel.interpreter.emit(Intent.SaveState(listComponent.getList()))
        }
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        if (!viewModel.created) {
            lifecycleScope.launch {
                viewModel.interpreter.emit(Intent.Create)
                viewModel.interpreter.emit(Intent.LoadMore)
            }
        }
    }
}