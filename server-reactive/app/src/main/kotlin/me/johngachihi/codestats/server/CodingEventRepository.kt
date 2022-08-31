package me.johngachihi.codestats.server

import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface CodingEventRepository : CoroutineCrudRepository<CodingEventDataModel, String>