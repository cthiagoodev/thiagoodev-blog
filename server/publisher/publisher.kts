#!/usr/bin/env kotlin

import java.io.File

val APP_VERSION = "v1.0.0"
val API_URL = "http://localhost:8080/api"
val RESET = "\u001B[0m"
val CYAN = "\u001B[36m"
val YELLOW = "\u001B[33m"
val GREEN = "\u001B[32m"
val RED = "\u001B[31m"
val BOLD = "\u001B[1m"

val banner = """
$CYAN
  ____  _             ____        _     _ _     _               
 | __ )| | ___   __ _|  _ \ _   _| |__ | (_)___| |__   ___ _ __ 
 |  _ \| |/ _ \ / _` | |_) | | | | '_ \| | / __| '_ \ / _ \ '__|
 | |_) | | (_) | (_| |  __/| |_| | |_) | | \__ \ | | |  __/ |   
 |____/|_|\___/ \__, |_|    \__,_|_.__/|_|_|___/_| |_|\___|_|   
                |___/                                           
$YELLOW >> Blog Publisher Tool $APP_VERSION $RESET
"""

fun main() {
    println(banner)
    println("${BOLD}MAIN MENU$RESET")
    println("----------------------------")
    println("${CYAN}1.$RESET Generate Template")
    println("${CYAN}2.$RESET Publish Article")
    println("----------------------------")

    print("${BOLD}Select an option:$RESET ")

    when (readln()) {
        "1" -> handleGenerateTemplate()
        "2" -> handlePublishFlow()
        else -> println("\n$RED[!] Invalid option. Please restart the script.$RESET")
    }
}

fun handleGenerateTemplate() {
    println("\n$YELLOW[#] Generating template...$RESET")

    val template = """
    {
      "title": "[Your Title]",
      "description": "[Your Description]",
      "tags": [],
      "image": "[Your Image URL]",
      "text": "[Your Text In MD]"
    }
    """.trimIndent()

    val file = File("post.json")
    file.writeText(template)

    println("$GREEN[✓] File '${file.name}' created successfully!$RESET\n")
}

fun handlePublishFlow() {
    println("\n$YELLOW[#] Starting publication flow...$RESET")
    val file = File("post.json")

    if (file.exists()) {
        val token = authenticate()
        if (token != null) {
            publish(file, token)
        }
    } else {
        println("$RED[X] Error: 'post.json' not found.$RESET")
    }
}

fun authenticate(): String? {
    println("\n$BOLD--- Credentials Required ---$RESET")

    print("$CYAN[?] Email:$RESET ")
    val email = readln()

    val console = System.console()
    val password = if (console != null) {
        print("$CYAN[?] Password (hidden):$RESET ")
        console.readPassword().joinToString("")
    } else {
        print("$CYAN[?] Password:$RESET ")
        readln()
    }

    print("\n$YELLOW[#] Authenticating user '$email'...$RESET ")

    Thread.sleep(800)

    if (email.isNotEmpty() && password.isNotEmpty()) {
        println("$GREEN[OK]$RESET")
        return "mock-token-${email.hashCode()}"
    } else {
        println("$RED[FAILED]$RESET")
        println("$RED[!] Email or password cannot be empty.$RESET")
        return null
    }
}

fun publish(file: File, token: String) {
    println("$CYAN[i] Publishing '${file.name}'...$RESET")

    Thread.sleep(1000)

    if(file.exists()) {
        file.delete()
        println("$GREEN[✓] Article published and local file cleaned up!$RESET\n")
    }
}

main()