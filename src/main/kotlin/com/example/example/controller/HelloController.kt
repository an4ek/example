package com.example.example.controller
import com.example.example.dto.GreetingMain
import com.example.example.dto.User
import com.example.example.dto.UserData
import com.example.example.dto.GreetingUser
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@RestController
@RequestMapping("/greeting")
class HelloController {

    private val users: MutableMap<String, User> = ConcurrentHashMap()

    @GetMapping
    fun hello(@RequestParam(required = false) id: String?): ResponseEntity<*> {
        return if (id != null) {
            val user = users[id]
            if (user != null) {
                ResponseEntity.ok(UserData(user.name, user.surname))
            } else {
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found")
            }
        } else {
            ResponseEntity.ok(GreetingMain())
        }
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: String): ResponseEntity<*> {
        val user = users[id]
        return if (user != null) {
            ResponseEntity.ok(UserData(user.name, user.surname))
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found")
        }
    }

    @PostMapping
    fun createUser(@RequestBody userData: UserData): ResponseEntity<GreetingUser> {
        val userId = UUID.randomUUID().toString()
        val user = User(
            id = userId,
            name = userData.name,
            surname = userData.surname
        )
        users[userId] = user

        val response = GreetingUser(
            text = "Hello, ${userData.surname} ${userData.name}",
            id = userId
        )
        return ResponseEntity.ok(response)
    }
}