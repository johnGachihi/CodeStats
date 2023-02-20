package me.johngachihi.codestats.server

import kotlinx.coroutines.flow.Flow
import me.johngachihi.codestats.core.CodingEventType
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
interface CodingActivityRepository : CoroutineCrudRepository<CodingEventDataModel, String> {
    @Query("{ 'type': ?0, 'firedAt': { \$gte: ?1, \$lt: ?2 } }")
    fun findAllRecordedBetween(
        type: CodingEventType,
        from: Instant,
        toExclusive: Instant
    ): Flow<CodingEventDataModel>

    suspend fun existsByUsername(username: String): Boolean
}