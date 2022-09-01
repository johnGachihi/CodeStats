package me.johngachihi.codestats.server

import com.fasterxml.jackson.databind.ObjectMapper
import me.johngachihi.codestats.core.CodingEvent
import me.johngachihi.codestats.core.CodingEventType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.Instant

@SpringBootTest
@AutoConfigureMockMvc
class CodingEventControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var codingEventRepository: CodingEventRepository

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun test() {
        val codingEvents = listOf(CodingEvent(CodingEventType.CHAR_TYPED, "a", Instant.now()))

        mockMvc.perform(
            post("/coding-event")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(codingEvents))
        )
            .andExpect(status().isOk)

        assertEquals(1, codingEventRepository.count())
        codingEventRepository.findAll().forEach(::println)
        assertEquals(codingEvents.first().type, codingEventRepository.findAll().first().type)
    }
}