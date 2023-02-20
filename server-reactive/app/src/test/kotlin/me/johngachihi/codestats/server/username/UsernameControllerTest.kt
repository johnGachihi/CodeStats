package me.johngachihi.codestats.server.username

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.reactive.server.WebTestClient

@WebFluxTest(UsernameController::class)
class UsernameControllerTest {
    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockBean
    private lateinit var checkUsernameExistsUseCase: CheckUsernameExistsUseCase

    @Test
    fun `when username exists returns a 204`() = runTest {
        Mockito.`when`(
            checkUsernameExistsUseCase.invoke("existing-username")
        ).thenReturn(true)

        webTestClient.get().uri("/username/existing-username/exists")
            .exchange()
            .expectStatus().isNoContent
    }

    @Test
    fun `when username does not exist returns a 404`() = runTest {
        Mockito.`when`(
            checkUsernameExistsUseCase.invoke("non-existent-username")
        ).thenReturn(false)

        webTestClient.get().uri("/username/non-existent-username/exists")
            .exchange()
            .expectStatus().isNotFound
    }
}