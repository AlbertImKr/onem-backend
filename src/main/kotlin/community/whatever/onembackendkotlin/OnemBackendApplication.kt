package community.whatever.onembackendkotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class OnemBackendKotlinApplication

fun main(args: Array<String>) {
    runApplication<OnemBackendKotlinApplication>(*args)
}
