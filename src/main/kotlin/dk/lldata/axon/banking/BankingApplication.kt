package dk.lldata.axon.banking

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BankingApplication

fun main(args: Array<String>) {
    runApplication<BankingApplication>(*args)
}
