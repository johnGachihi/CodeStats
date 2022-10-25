package me.johngachihi.codestats.server.typing

import kotlinx.coroutines.test.runTest
import me.johngachihi.codestats.server.makeCharTypedEvent
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class DefaultGetTypedCharCountUseCaseTest {
    @Test
    fun `returns count of entities gotten from GetTypingActivityUseCase`() = runTest {
        val mockGetTypingActivity = MockGetTypingActivityUseCase(
            makeCharTypedEvent(),
            makeCharTypedEvent(),
            makeCharTypedEvent(),
        )

        val count = DefaultGetTypedCharCountUseCase(mockGetTypingActivity)
            .invoke(LocalDate.EPOCH, Period.Day)

        assertThat(count).isEqualTo(3)
    }

}