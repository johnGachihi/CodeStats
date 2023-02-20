package me.johngachihi.codestats.server.username

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.test.runTest
import me.johngachihi.codestats.server.CodingActivityRepository
import me.johngachihi.codestats.server.makeCharTypedEvent
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.data.mongodb.core.ReactiveMongoTemplate

@DataMongoTest
class CheckUsernameExistsUseCaseTest {
    @Autowired
    private lateinit var mongoTemplate: ReactiveMongoTemplate

    @Autowired
    private lateinit var codingActivityRepository: CodingActivityRepository

    @Test
    fun `when username is a property of existing coding activity returns true`() = runTest {
        mongoTemplate.insert(
            makeCharTypedEvent(username = "in-use-username")
        ).asFlow().collect()

        val exists = CheckUsernameExistsUseCase(codingActivityRepository)
            .invoke("in-use-username")

        assertThat(exists).isTrue()
    }

    @Test
    fun `when username is not a property of any existing coding event returns false`() = runTest {
        val exists = CheckUsernameExistsUseCase(codingActivityRepository)
            .invoke("in-use-username")

        assertThat(exists).isFalse()
    }
}