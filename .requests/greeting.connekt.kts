val baseUrl: String by env

println("=== 1: GET /greeting")
GET("$baseUrl/greeting") {
    accept("application/json")
} then {
    if (code == 200) {
        println("1 PASSED: Status 200, Response: $body")
    } else {
        println("1 FAILED: Expected 200, got $code")
    }
}



println("\n=== 2: POST /greeting")
var userId = ""

POST("$baseUrl/greeting") {
    contentType("application/json")
    body("""
        {
            "name": "Ivan",
            "surname": "Ivanov"
        }
    """.trimIndent())
} then {
    if (code == 200) {
        println("2 PASSED: Status 200")
        println("   Response: $body")

        // Извлекаем ID из строки JSON
        val responseStr = body.toString()
        val idPattern = """"id":"([^"]+)"""".toRegex()
        val match = idPattern.find(responseStr)
        if (match != null) {
            userId = match.groupValues[1]
            println("   Saved ID: $userId")
        }
    } else {
        println("2 FAILED: Expected 200, got $code")
    }
}


println("\n=== 3: GET /greeting?id=$userId")
GET("$baseUrl/greeting?id=$userId") {
    accept("application/json")
} then {
    if (code == 200) {
        println("3 PASSED: Status 200, Response: $body")
    } else {
        println("3 FAILED: Expected 200, got $code")
    }
}


println("\n=== 4: GET /greeting/$userId")
GET("$baseUrl/greeting/$userId") {
    accept("application/json")
} then {
    if (code == 200) {
        println("4 PASSED: Status 200, Response: $body")
    } else {
        println("4 FAILED: Expected 200, got $code")
    }
}


println("\n=== 5: GET (404)")
GET("$baseUrl/greeting?id=000000-0000-0") {
    accept("application/json")
} then {
    if (code == 404) {
        println("5 PASSED: Got 404 as expected")
    } else {
        println("5 FAILED: Expected 404, got $code")
    }
}