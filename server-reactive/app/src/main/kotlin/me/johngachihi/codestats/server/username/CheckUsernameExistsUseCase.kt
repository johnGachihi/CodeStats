package me.johngachihi.codestats.server.username

import me.johngachihi.codestats.server.CodingActivityRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CheckUsernameExistsUseCase(
    @Autowired val codingActivityRepository: CodingActivityRepository
) {
    suspend operator fun invoke(username: String): Boolean =
        codingActivityRepository.existsByUsername(username)
}