package me.johngachihi.codestats.server

import me.johngachihi.codestats.core.CodingEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin
@RequestMapping("/coding-event")
class CodingEventController {
    @Autowired
    private lateinit var codingEventRepository: CodingEventRepository

    @PostMapping
    suspend fun addLog(@RequestBody events: List<CodingEvent>) {
        codingEventRepository.saveAll(events.map {
            codingEventToCodingEventDataModel(it)
        })
    }
}