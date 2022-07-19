package me.johngachihi.codestats.server

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class LogControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var logRepository: LogRepository

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun test() {
        val logEntry = LogEntry("an-event")
        mockMvc.perform(
            post("/log")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(logEntry))
        )
            .andExpect(status().isOk)

        assertEquals(1, logRepository.count())
        assertEquals(logEntry.event, logRepository.findAll().first().event)
    }
}