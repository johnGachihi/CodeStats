package me.johngachihi.codestats.server

import me.johngachihi.codestats.core.CodingEvent
import me.johngachihi.codestats.core.CodingEventType
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.PersistenceCreator
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("coding_event")
data class CodingEventDataModel @PersistenceCreator constructor(
    @Id val id: String?,
    val type: CodingEventType,
    val payload: String,
    val firedAt: Instant,
    val username: String?,
    val language: String?
) {
    constructor(
        type: CodingEventType,
        payload: String,
        firedAt: Instant,
        username: String?,
        language: String?
    ) : this(null, type, payload, firedAt, username, language)
}

fun CodingEvent.toCodingEventDataModel() =
    CodingEventDataModel(type, payload, firedAt, username, language)
