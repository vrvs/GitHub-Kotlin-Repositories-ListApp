package com.github.vrvs.githubapp.uicomponent.repository

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.vrvs.githubapp.R
import com.github.vrvs.githubapp.uicomponent.repository.adapter.RepositoryAdapter
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class RepositoryListComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): FrameLayout(context, attrs, defStyleAttr), SwipeRefreshLayout.OnRefreshListener {

    sealed class Action {
        data class ItemClicked(val id: String): Action()
        object LoadMoreData: Action()
        object Refresh: Action()
    }

    private val list: RecyclerView
        get() = findViewById(R.id.list_repositories)
    private val swipeRefresh: SwipeRefreshLayout
        get() = findViewById(R.id.swipe_container)

    private var loading = true
    private var pastVisibleItems: Int = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0
    private var mLayoutManager: LinearLayoutManager

    private val observer: Observer<String>
    private val privateOutput = MutableLiveData<Action>()
    val output: LiveData<Action> = privateOutput

    init {

        LayoutInflater.from(context).inflate(R.layout.component_repository_list, this, true)
        mLayoutManager = LinearLayoutManager(context)

        list.layoutManager = mLayoutManager

        observer = Observer {
            privateOutput.postValue(Action.ItemClicked(it))
        }
        list.adapter = RepositoryAdapter(context).also {
            it.itemClicked.observeForever(observer)
        }

        list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(
                recyclerView: RecyclerView,
                dx: Int,
                dy: Int
            ) {
                if (dy > 0) {
                    visibleItemCount = mLayoutManager.childCount
                    totalItemCount = mLayoutManager.itemCount
                    pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition()
                    if (loading) {
                        if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                            loading = false
                            swipeRefresh.isRefreshing = true
                            privateOutput.postValue(Action.LoadMoreData)
                        }
                    }
                }
            }
        })
        swipeRefresh.setOnRefreshListener(this)
    }

    fun error() {
        swipeRefresh.isRefreshing = false
    }

    fun getList(): List<RepositoryComponent.ComponentModel> {
        return (list.adapter as RepositoryAdapter).dataSet
    }

    fun changeDataSet(dataSet: List<RepositoryComponent.ComponentModel>) {
        swipeRefresh.isRefreshing = false
        loading = true
        (list.adapter as RepositoryAdapter).dataSet = dataSet
    }

    fun appendDataSet(dataSet: List<RepositoryComponent.ComponentModel>) {
        loading = true
        swipeRefresh.isRefreshing = false
        val values = (list.adapter as RepositoryAdapter).dataSet.toMutableList()
        values.addAll(dataSet)
        (list.adapter as RepositoryAdapter).dataSet = values
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        (list.adapter as RepositoryAdapter).itemClicked.removeObserver(observer)
    }

    override fun onRefresh() {
        privateOutput.postValue(Action.Refresh)
    }

    override fun onSaveInstanceState(): Parcelable? {
        return super.onSaveInstanceState()
    }
}
