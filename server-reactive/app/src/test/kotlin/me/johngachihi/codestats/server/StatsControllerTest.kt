package me.johngachihi.codestats.server

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runTest

import me.johngachihi.codestats.core.CodingEventType

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

import java.time.Instant
import java.time.temporal.ChronoUnit

@SpringBootTest
@AutoConfigureWebTestClient
internal class StatsControllerTest {
    @Autowired
    private lateinit var webClient: WebTestClient

    @Autowired
    private lateinit var codingEventRepository: CodingEventRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `stats endpoint returns the count for char-typed events fired today`() = runTest {
        codingEventRepository.saveAll(
            listOf(
                CodingEventDataModel(CodingEventType.CHAR_TYPED, "a", Instant.now()),
                CodingEventDataModel(CodingEventType.CHAR_TYPED, "b", Instant.now()),

                CodingEventDataModel(
                    CodingEventType.CHAR_TYPED,
                    "a",
                    firedAt = Instant.now().minus(1, ChronoUnit.DAYS)
                ),
                CodingEventDataModel(CodingEventType.PASTE, "abc", firedAt = Instant.now())
            )
        ).collect()

        webClient.get().uri("/stats")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.typedCharsToday").isEqualTo(2)
    }
}