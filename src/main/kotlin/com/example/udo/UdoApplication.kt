package com.example.udo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class UdoApplication

fun main(args: Array<String>) {
	runApplication<UdoApplication>(*args)
}
