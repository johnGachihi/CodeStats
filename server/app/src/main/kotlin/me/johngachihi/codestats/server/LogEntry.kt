package me.johngachihi.codestats.server

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.PersistenceCreator
import org.springframework.data.mongodb.core.mapping.Document

@Document("log_entries")
data class LogEntry @PersistenceCreator constructor(@Id val id: String?, val event: String) {
    constructor(event: String): this(null, event)
}

/*
    Event:
        Event type eg, Type, Paste (Could be enum for type-safety)
        Event Payload eg, for Type event, typed character; for Paste event, pasted text

    TimeStamp: ISO-80... UTC timestamp of when event happened

    Could rename the entity to `Event` with properties
        - type: Enum(Type, Paste, ...)
        - payload: Depending on `type`
        - firedAt: java.time.Instant

    Sealed class Event<T>(@Id id: String?, type: String, payload: T, firedAt: java.time.Instant)
    class TypeEvent(@Id id: String?, payload: Char, firedAt: java.time.Instant)
        : Event<Char>(id, type = "Type", payload, firedAt)

    CodeEvent, CodeStatEvent, *CodingEvent*,
*/