package me.johngachihi.codestats.server.typing

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import me.johngachihi.codestats.core.Period
import me.johngachihi.codestats.server.CodingEventDataModel
import java.time.LocalDate

class MockGetTypingActivityUseCase(
    private vararg val returning: CodingEventDataModel
) : GetTypingActivityUseCase {

    data class Params(
        val day: LocalDate,
        val period: Period,
        val username: String? = null
    )

    private val _calls = mutableListOf<Params>()
    val calls: List<Params> = _calls

    override fun invoke(
        day: LocalDate,
        period: Period,
        username: String?
    ): Flow<CodingEventDataModel> {
        _calls.add(Params(day, period, username))
        return flowOf(*returning)
    }
}