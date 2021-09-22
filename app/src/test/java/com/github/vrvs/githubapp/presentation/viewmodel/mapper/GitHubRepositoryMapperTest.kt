package com.github.vrvs.githubapp.presentation.viewmodel.mapper

import com.github.vrvs.githubapp.Fixture.REPOSITORIES_COMPONENT_MODEL_LIST
import com.github.vrvs.githubapp.Fixture.REPOSITORIES_ENTITY_LIST
import org.junit.Test
import kotlin.test.assertEquals

class GitHubRepositoryMapperTest {

    @Test
    fun `to repository component model should map values correctly`() {
        assertEquals(
            expected = REPOSITORIES_COMPONENT_MODEL_LIST,
            actual = REPOSITORIES_ENTITY_LIST.map {
                GitHubRepositoryMapper.toRepositoryComponentModel(it)
            }
        )
    }
}