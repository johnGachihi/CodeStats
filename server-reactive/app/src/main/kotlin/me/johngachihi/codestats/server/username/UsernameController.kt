package me.johngachihi.codestats.server.username

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/username")
class UsernameController {
    @Autowired
    private lateinit var checkUsernameExists: CheckUsernameExistsUseCase

    @GetMapping("/{username}/exists")
    suspend fun checkExists(@PathVariable username: String): ResponseEntity<Unit> {
        return if (checkUsernameExists(username))
            ResponseEntity(HttpStatus.NO_CONTENT)
        else
            ResponseEntity(HttpStatus.NOT_FOUND)
    }
}