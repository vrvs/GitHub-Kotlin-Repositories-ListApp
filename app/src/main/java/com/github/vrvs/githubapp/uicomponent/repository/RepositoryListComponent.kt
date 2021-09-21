package com.github.vrvs.githubapp.uicomponent.repository

import android.content.Context
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

class RepositoryListComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): FrameLayout(context, attrs, defStyleAttr) {

    private val list: RecyclerView
        get() = findViewById(R.id.list_repositories)

    private val observer: Observer<String>
    private val _itemClicked = MutableLiveData<String>()
    val itemClicked: LiveData<String> = _itemClicked

    init {
        LayoutInflater.from(context).inflate(R.layout.component_repository_list, this, true)
        list.layoutManager = LinearLayoutManager(context)
        observer = Observer {
            _itemClicked.postValue(it)
        }
        list.adapter = RepositoryAdapter(context).also {
            it.itemClicked.observeForever(observer)
        }
    }

    fun changeDataSet(dataSet: List<RepositoryComponent.ComponentModel>) {
        (list.adapter as RepositoryAdapter).dataSet = dataSet
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        (list.adapter as RepositoryAdapter).itemClicked.removeObserver(observer)
    }
}
