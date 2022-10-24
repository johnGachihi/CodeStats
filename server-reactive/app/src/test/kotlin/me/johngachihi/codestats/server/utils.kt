package me.johngachihi.codestats.server

import me.johngachihi.codestats.core.CodingEventType
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

fun makeCharTypedEvent(
    payload: Char = 'a',
    firedAt: Instant = Instant.now()
) = CodingEventDataModel(
    type = CodingEventType.CHAR_TYPED,
    payload = payload.toString(),
    firedAt = firedAt
)

fun makePasteEvent(
    payload: String = "abc",
    firedAt: Instant = Instant.now()
) = CodingEventDataModel(
    type = CodingEventType.PASTE,
    payload = payload,
    firedAt = firedAt
)

val Int.days: Duration
    get() = Duration.ofHours(this * 24L)

val Int.hours: Duration
    get() = Duration.ofHours(this.toLong())

val Int.min: Duration
    get() = Duration.ofMinutes(this.toLong())

val Int.nanoSecs: Duration
    get() = Duration.ofNanos(this.toLong())

val LocalDate.startOfDay: Instant
    get() = atStartOfDay().toInstant(ZoneOffset.UTC)

fun startOfToday(now: Instant): Instant = now.truncatedTo(ChronoUnit.DAYS)

fun startOfTomorrow(now: Instant): Instant = startOfToday(now) + 24.hours

fun justYesterday(now: Instant): Instant = startOfToday(now) - 1.nanoSecs