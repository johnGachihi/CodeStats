package me.johngachihi.codestats.server.typing

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import me.johngachihi.codestats.core.TypingStats
import me.johngachihi.codestats.server.typing.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import kotlin.coroutines.coroutineContext

@RestController
@RequestMapping("/activity/typing")
class TypingStatsController {
    @Autowired
    private lateinit var getTypingRate: GetTypingRateUseCase

    @Autowired
    private lateinit var getTypedCharCount: GetTypedCharCountUseCase

    @GetMapping("/{day}/{period}")
    suspend fun all(
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) day: LocalDate,
        @PathVariable period: Period
    ): TypingStats {
        val coroutineScope = CoroutineScope(coroutineContext)
        val rate = coroutineScope.async { getTypingRate(day, period) }
        val count = coroutineScope.async { getTypedCharCount(day, period) }

        return TypingStats(count = count.await(), rate = rate.await())
    }
}