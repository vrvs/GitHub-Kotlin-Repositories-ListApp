package com.github.vrvs.githubapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.github.vrvs.githubapp.uicomponent.repository.RepositoryComponent
import com.github.vrvs.githubapp.uicomponent.repository.RepositoryListComponent

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val component = findViewById<RepositoryListComponent>(R.id.repo_list_component)
        component.changeDataSet(
            listOf(
                RepositoryComponent.ComponentModel(
                    id = "id1",
                    repoName = "Repository 1",
                    authorName = "Author 1",
                    forksNumber = 5,
                    starsNumber = 10,
                    imageUrl = "https://avatars.githubusercontent.com/u/23401985?s=48&v=4"
                ),
                RepositoryComponent.ComponentModel(
                    id = "id2",
                    repoName = "Repository 1",
                    authorName = "Author 1",
                    forksNumber = 5,
                    starsNumber = 10,
                    imageUrl = "https://avatars.githubusercontent.com/u/23401985?s=48&v=4"
                ),
                RepositoryComponent.ComponentModel(
                    id = "id3",
                    repoName = "Repository 1",
                    authorName = "Author 1",
                    forksNumber = 5,
                    starsNumber = 10,
                    imageUrl = "https://avatars.githubusercontent.com/u/23401985?s=48&v=4"
                ),
                RepositoryComponent.ComponentModel(
                    id = "id4",
                    repoName = "Repository 1",
                    authorName = "Author 1",
                    forksNumber = 5,
                    starsNumber = 10,
                    imageUrl = "https://avatars.githubusercontent.com/u/23401985?s=48&v=4"
                ),
                RepositoryComponent.ComponentModel(
                    id = "id5",
                    repoName = "Repository 1",
                    authorName = "Author 1",
                    forksNumber = 5,
                    starsNumber = 10,
                    imageUrl = "https://avatars.githubusercontent.com/u/23401985?s=48&v=4"
                ),
                RepositoryComponent.ComponentModel(
                    id = "id6",
                    repoName = "Repository 1",
                    authorName = "Author 1",
                    forksNumber = 5,
                    starsNumber = 10,
                    imageUrl = "https://avatars.githubusercontent.com/u/23401985?s=48&v=4"
                ),
                RepositoryComponent.ComponentModel(
                    id = "id7",
                    repoName = "Repository 1",
                    authorName = "Author 1",
                    forksNumber = 5,
                    starsNumber = 10,
                    imageUrl = "https://avatars.githubusercontent.com/u/23401985?s=48&v=4"
                ),
                RepositoryComponent.ComponentModel(
                    id = "id8",
                    repoName = "Repository 1",
                    authorName = "Author 1",
                    forksNumber = 5,
                    starsNumber = 10,
                    imageUrl = "https://avatars.githubusercontent.com/u/23401985?s=48&v=4"
                ),
                RepositoryComponent.ComponentModel(
                    id = "id9",
                    repoName = "Repository 1",
                    authorName = "Author 1",
                    forksNumber = 5,
                    starsNumber = 10,
                    imageUrl = "https://avatars.githubusercontent.com/u/23401985?s=48&v=4"
                ),
                RepositoryComponent.ComponentModel(
                    id = "id10",
                    repoName = "Repository 1",
                    authorName = "Author 1",
                    forksNumber = 5,
                    starsNumber = 10,
                    imageUrl = "https://avatars.githubusercontent.com/u/23401985?s=48&v=4"
                )
            )
        )
        component.itemClicked.observeForever {
            Toast.makeText(applicationContext, it, LENGTH_SHORT).show()
        }
    }
}