package me.johngachihi.codestats.server.typing

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.test.runTest
import me.johngachihi.codestats.core.CodingEventType
import me.johngachihi.codestats.server.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.test.annotation.DirtiesContext
import java.time.DayOfWeek.MONDAY
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters.*

@DataMongoTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
internal class GetTypingActivityUseCaseTest {

    @Autowired
    private lateinit var mongoTemplate: ReactiveMongoTemplate

    @Autowired
    private lateinit var codingActivityRepository: CodingActivityRepository

    private lateinit var aDay: LocalDate

    @BeforeEach
    fun initNow() {
        aDay = LocalDate.now()
    }

    @Test
    fun `gets only typing activity`() = runTest {
        mongoTemplate.insertAll(
            listOf(
                makeCharTypedEvent(firedAt = aDay.startOfDay),
                makePasteEvent(firedAt = aDay.startOfDay)
            )
        ).asFlow().collect()

        val activity = DefaultGetTypingActivityUseCase(codingActivityRepository)
            .invoke(day = aDay, period = Period.Day)
            .toList()

        assertThat(activity).hasSize(1)
        assertThat(activity.first().type)
            .isInstanceOf(CodingEventType.CHAR_TYPED::class.java)
    }

    @Test
    fun `when day's activity is queried, returns activity from start to end of specified day only`() = runTest {
        val startOfDay = aDay.startOfDay
        val startOfNextDay = startOfDay + 24.hours
        mongoTemplate.insertAll(
            listOf(
                makeCharTypedEvent(payload = 'n', firedAt = startOfDay - 1.nanoSecs), // Yesterday
                makeCharTypedEvent(payload = 'y', firedAt = startOfDay),
                makeCharTypedEvent(payload = 'y', firedAt = startOfNextDay - 1.nanoSecs), // End of day
                makeCharTypedEvent(payload = 'n', firedAt = startOfNextDay)
            )
        ).asFlow().collect()

        val activity = DefaultGetTypingActivityUseCase(codingActivityRepository)
            .invoke(day = aDay, period = Period.Day)
            .toList()

        assertThat(activity.map { it.payload }).containsExactlyInAnyOrder("y", "y")
    }

    @Test
    fun `when week's activity is queried, returns activity from start to end of the week the specified day is in`() =
        runTest {
            val startOfWeek = aDay.with(previousOrSame(MONDAY)).startOfDay
            val startOfNextWeek = aDay.with(next(MONDAY)).startOfDay
            mongoTemplate.insertAll(
                listOf(
                    makeCharTypedEvent(payload = 'n', firedAt = startOfWeek - 1.nanoSecs),
                    makeCharTypedEvent(payload = 'y', firedAt = startOfWeek),
                    makeCharTypedEvent(payload = 'y', firedAt = startOfNextWeek - 1.nanoSecs),
                    makeCharTypedEvent(payload = 'n', firedAt = startOfNextWeek)
                )
            ).asFlow().collect()

            val activity = DefaultGetTypingActivityUseCase(codingActivityRepository)
                .invoke(day = aDay, period = Period.Week)
                .toList()

            assertThat(activity.map { it.payload }).containsExactlyInAnyOrder("y", "y")
        }

    @Test
    fun `when month's activity is queried, returns activity from start to end of month specified day is in`() =
        runTest {
            val startOfMonth = aDay.with(firstDayOfMonth()).startOfDay
            val startOfNextMonth = aDay.with(firstDayOfNextMonth()).startOfDay
            mongoTemplate.insertAll(
                listOf(
                    makeCharTypedEvent(payload = 'n', firedAt = startOfMonth - 1.nanoSecs),
                    makeCharTypedEvent(payload = 'y', firedAt = startOfMonth),
                    makeCharTypedEvent(payload = 'y', firedAt = startOfNextMonth - 1.nanoSecs),
                    makeCharTypedEvent(payload = 'n', firedAt = startOfNextMonth)
                ),
            ).asFlow().collect()

            val activity = DefaultGetTypingActivityUseCase(codingActivityRepository)
                .invoke(day = aDay, period = Period.Month)
                .toList()

            assertThat(activity.map { it.payload }).containsExactlyInAnyOrder("y", "y")
        }
}