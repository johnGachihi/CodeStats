package me.johngachihi.codestats.server

import me.johngachihi.codestats.core.CodingEventType

import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

import java.time.Instant

interface CodingEventRepository : CoroutineCrudRepository<CodingEventDataModel, String> {

    @Query("{ 'type': ?0, 'firedAt': { \$gte: ?1, \$lt: ?2 } }", count = true)
    suspend fun countEventsFiredBetween(
        type: CodingEventType,
        from: Instant,
        toExclusive: Instant
    ): Int
}