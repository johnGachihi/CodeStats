package me.johngachihi.codestats.server.typing

import kotlinx.coroutines.test.runTest
import me.johngachihi.codestats.core.Period
import me.johngachihi.codestats.core.TypingRateSample
import me.johngachihi.codestats.core.TypingStats
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month

@WebFluxTest(TypingStatsController::class)
internal class TypingStatsEndpointTest {
    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockBean
    private lateinit var getTypingRateUseCase: GetTypingRateUseCase

    @MockBean
    private lateinit var getTypedCharCount: GetTypedCharCountUseCase

    @Test
    fun `calls use-cases correctly`() = runTest {
        `when`(
            getTypingRateUseCase.invoke(LocalDate.parse("2022-10-24"), Period.Day, "a-username")
        ).thenReturn(emptyList())
        `when`(
            getTypedCharCount.invoke(LocalDate.parse("2022-10-24"), Period.Day, "a-username")
        ).thenReturn(0)

        webTestClient.get().uri("/activity/typing/2022-10-24/Day?username=a-username")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk

        verify(getTypingRateUseCase)
            .invoke(LocalDate.parse("2022-10-24"), Period.Day, "a-username")
        verify(getTypedCharCount)
            .invoke(LocalDate.parse("2022-10-24"), Period.Day, "a-username")

        //

        `when`(
            getTypingRateUseCase.invoke(LocalDate.parse("2022-10-24"), Period.Day, null)
        ).thenReturn(emptyList())
        `when`(
            getTypedCharCount.invoke(LocalDate.parse("2022-10-24"), Period.Day, null)
        ).thenReturn(0)

        webTestClient.get().uri("/activity/typing/2022-10-24/Day")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk

        verify(getTypingRateUseCase)
            .invoke(LocalDate.parse("2022-10-24"), Period.Day, null)
        verify(getTypedCharCount)
            .invoke(LocalDate.parse("2022-10-24"), Period.Day, null)
    }

    @Test
    fun `returns what it gets from use cases`() = runTest {
        val expectedTypingStats = TypingStats(
            count = 10,
            rate = listOf(
                TypingRateSample(lowerLimit = LocalDateTime.of(2022, Month.APRIL, 1, 12, 0), count = 1),
                TypingRateSample(lowerLimit = LocalDateTime.of(2022, Month.APRIL, 2, 12, 0), count = 3),
            )
        )
        `when`(
            getTypingRateUseCase.invoke(LocalDate.parse("2022-10-24"), Period.Day)
        ).thenReturn(expectedTypingStats.rate)
        `when`(
            getTypedCharCount.invoke(LocalDate.parse("2022-10-24"), Period.Day)
        ).thenReturn(expectedTypingStats.count)

        webTestClient.get().uri("/activity/typing/2022-10-24/Day")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody(TypingStats::class.java)
            .value { assertThat(it).isEqualTo(expectedTypingStats) }
    }
}