package me.johngachihi.codestats.server

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import me.johngachihi.codestats.core.CodingEvent
import me.johngachihi.codestats.core.CodingEventType
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.Instant
import java.time.temporal.ChronoUnit

@SpringBootTest
@DirtiesContext
@AutoConfigureWebTestClient
class CodingEventsTest {
    @Autowired
    private lateinit var webClient: WebTestClient

    @Autowired
    private lateinit var codingEventRepository: CodingEventRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `received coding event is persisted`() = runTest {
        val codingEvent = CodingEvent(
            type = CodingEventType.CHAR_TYPED,
            payload = "a",
            firedAt = Instant.now()
        )
        webClient.post().uri("/coding-event")
            .bodyValue(codingEvent)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk

        assertThat(codingEventRepository.count()).isEqualTo(1)
        val storedEvent = codingEventRepository.findAll().first()
        assertThat(storedEvent)
            .hasFieldOrPropertyWithValue("type", codingEvent.type)
            .hasFieldOrPropertyWithValue("payload", codingEvent.payload)
        assertThat(storedEvent.firedAt)
            .isCloseTo(codingEvent.firedAt, within(1, ChronoUnit.MILLIS))
    }
}