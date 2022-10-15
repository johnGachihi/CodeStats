package me.johngachihi.codestats.server

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class StatsController {
    @Autowired
    private lateinit var statsService: CodingStatsService

    @GetMapping("/stats")
    suspend fun stats(): CodingStats = statsService.computeStats()
}