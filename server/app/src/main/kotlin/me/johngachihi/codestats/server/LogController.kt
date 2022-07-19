package me.johngachihi.codestats.server

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin
@RequestMapping("/log")
class LogController {
    @Autowired
    private lateinit var logRepository: LogRepository

    @PostMapping
    fun addLog(@RequestBody log: LogEntry) {
        logRepository.save(log)
    }
}