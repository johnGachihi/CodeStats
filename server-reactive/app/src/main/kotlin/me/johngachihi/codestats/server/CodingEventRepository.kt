package me.johngachihi.codestats.server

import me.johngachihi.codestats.core.CodingEventType
import org.springframework.data.mongodb.repository.Aggregation

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

    @Aggregation(
        pipeline = [
            "{ \$match: { type: ?0 } }",
            "{ \$match: { firedAt: { \$gte: ?1, \$lt: ?2 } } }",
            """
            {
              ${"$"}addFields: {
                timeInMinutes: {
                  ${"$"}add: [
                    { ${"$"}multiply: [{${"$"}hour: '${"$"}firedAt'}, 60]},
                    { ${"$"}minute: '${"$"}firedAt' }
                  ]
                }
              }
            }
            """,
            """
            {
              ${"$"}addFields: {
                x: { ${"$"}floor: { ${"$"}divide: ['${"$"}timeInMinutes', 30] } }
              }
            }
            """,
            "{ \$group: { _id: '\$x', y: { \$count: {} } } }",
            "{ \$project: { x: '\$_id', y: 1 } }"
        ]
    )
    suspend fun getEventDistributionBetween(
        type: CodingEventType,
        from: Instant,
        toExclusive: Instant
    ): List<DistributionEntry>
}

data class DistributionEntry(
    val x: Int,
    val y: Int
)