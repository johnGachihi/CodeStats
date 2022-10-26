package me.johngachihi.codestats.server

import kotlinx.coroutines.flow.Flow
import me.johngachihi.codestats.core.CodingEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class CodingEventController {
    @Autowired
    private lateinit var codingActivityRepo: CodingActivityRepository

    @PostMapping("/coding-event")
    suspend fun logCodingEvent(@RequestBody codingEvent: Flow<CodingEvent>) {
        codingEvent.collect {
            codingActivityRepo.save(it.toCodingEventDataModel())
        }
    }
}