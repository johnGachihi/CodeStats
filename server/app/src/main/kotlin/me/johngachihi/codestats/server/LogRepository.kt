package me.johngachihi.codestats.server

import org.springframework.data.mongodb.repository.MongoRepository

interface LogRepository : MongoRepository<LogEntry, String>