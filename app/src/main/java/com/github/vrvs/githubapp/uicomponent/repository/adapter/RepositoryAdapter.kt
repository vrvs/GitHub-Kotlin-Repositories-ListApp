package com.github.vrvs.githubapp.uicomponent.repository.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.github.vrvs.githubapp.uicomponent.repository.RepositoryComponent

internal class RepositoryAdapter(val context: Context) : RecyclerView.Adapter<RepositoryAdapter.ViewHolder>() {

    var dataSet: List<RepositoryComponent.ComponentModel> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val _itemClicked = MutableLiveData<String>()
    val itemClicked: LiveData<String> = _itemClicked

    class ViewHolder(var view: RepositoryComponent) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) =
        ViewHolder(
            RepositoryComponent(viewGroup.context)
        )

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.view.render(dataSet[position])

        // clicked item action
        viewHolder.itemView.setOnClickListener {
            _itemClicked.postValue(dataSet[position].id)
        }
    }

    override fun getItemCount() = dataSet.size
}