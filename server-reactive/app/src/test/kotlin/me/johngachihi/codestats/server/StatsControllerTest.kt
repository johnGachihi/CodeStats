package me.johngachihi.codestats.server

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runTest

import me.johngachihi.codestats.core.CodingEventType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.Duration

import java.time.Instant
import java.time.temporal.ChronoUnit

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureWebTestClient
@OptIn(ExperimentalCoroutinesApi::class)
internal class StatsControllerTest {
    @Autowired
    private lateinit var webClient: WebTestClient

    @Autowired
    private lateinit var codingEventRepository: CodingEventRepository

    // TODO: Extract time stuff
    lateinit var now: Instant

    @BeforeEach
    fun initCurrentTime() {
        now = Instant.now()
    }

    val startOfToday
        get() = now.truncatedTo(ChronoUnit.DAYS)

    val startOfTomorrow
        get() = startOfToday + 24.hours

    val justYesterday
        get() = startOfToday - 1.nanoSecs

    @Test
    fun `stats endpoint returns the count for char-typed events fired today`() = runTest {
        codingEventRepository.saveAll(
            listOf(
                createCharTypedEvent(firedAt = startOfToday),
                createCharTypedEvent(firedAt = startOfToday + 12.hours),
                createCharTypedEvent(firedAt = startOfToday + 24.hours - 1.nanoSecs),

                // Not counted
                createCharTypedEvent(firedAt = justYesterday),
                CodingEventDataModel(CodingEventType.PASTE, "abc", firedAt = startOfToday)
            )
        ).collect()

        webClient.get().uri("/stats")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody(CodingStats::class.java)
            .value {
                assertThat(it.typedCharsToday).isEqualTo(3)
            }
    }

    @Test
    fun `stats endpoint returns char-typed event count distribution over a day`() = runTest {
        codingEventRepository.saveAll(
            listOf(
                // 0000 <= x < 0030
                createCharTypedEvent(firedAt = startOfToday),
                createCharTypedEvent(firedAt = startOfToday + 15.min),
                createCharTypedEvent(firedAt = startOfToday + 30.min - 1.nanoSecs),
                // 0030 <= x < 0100
                createCharTypedEvent(firedAt = startOfToday + 30.min),
                createCharTypedEvent(firedAt = startOfToday + 1.hours - 1.nanoSecs),
                // 1200 <= x < 1230
                createCharTypedEvent(firedAt = startOfToday + 12.hours),
                // 2330 <= x < 2400
                createCharTypedEvent(firedAt = startOfToday + 23.hours + 45.min),
                // x > 2400 (Not included)
                createCharTypedEvent(firedAt = startOfTomorrow)
            )
        ).collect()

        webClient.get().uri("/stats")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody(CodingStats::class.java)
            .value {
                assertThat(it.typedCharsDistributionToday).containsExactlyInAnyOrder(
                    DistributionEntry(x = 0, y = 3), // 0000 <= x < 0030
                    DistributionEntry(x = 1, y = 2), // 0030 <= x < 0100
                    DistributionEntry(x = 24, y = 1), // 1200 <= x < 1230
                    DistributionEntry(x = 47, y = 1)  // 2330 <= x < 2400
                )
            }
    }

    private val Int.hours
        get() = Duration.ofHours(this.toLong())

    private val Int.min
        get() = Duration.ofMinutes(this.toLong())

    private val Int.nanoSecs
        get() = Duration.ofNanos(this.toLong())

    private fun createCharTypedEvent(
        payload: Char = 'a',
        firedAt: Instant = Instant.now()
    ) = CodingEventDataModel(
        type = CodingEventType.CHAR_TYPED,
        payload = payload.toString(),
        firedAt = firedAt
    )
}