package me.johngachihi.codestats.server.typing

import kotlinx.coroutines.flow.count
import me.johngachihi.codestats.core.Period
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate

interface GetTypedCharCountUseCase {
    suspend operator fun invoke(
        day: LocalDate,
        period: Period,
        username: String? = null
    ): Int
}

@Service
class DefaultGetTypedCharCountUseCase(
    @Autowired
    private val getTypingActivity: GetTypingActivityUseCase
) : GetTypedCharCountUseCase {

    override suspend fun invoke(day: LocalDate, period: Period, username: String?) =
        getTypingActivity(day, period, username).count()
}