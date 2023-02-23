package me.johngachihi.codestats.server.typing

import kotlinx.coroutines.test.runTest
import me.johngachihi.codestats.core.Period
import me.johngachihi.codestats.server.makeCharTypedEvent
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class DefaultGetTypedCharCountUseCaseTest {
    @Test
    fun `calls GetTypingActivityUseCase appropriately`() = runTest {
        val aDay = LocalDate.now()
        val getTypingActivityUseCase = MockGetTypingActivityUseCase()

        val getTypedCharCountUseCase = DefaultGetTypedCharCountUseCase(getTypingActivityUseCase)
        getTypedCharCountUseCase.invoke(LocalDate.now(), Period.Day)
        getTypedCharCountUseCase.invoke(LocalDate.now(), Period.Day, "a-username")

        assertThat(getTypingActivityUseCase.calls).containsExactlyInAnyOrder(
            MockGetTypingActivityUseCase.Params(aDay, Period.Day),
            MockGetTypingActivityUseCase.Params(aDay, Period.Day, username = "a-username"),
        )
    }

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