package co.selim.sawmill.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SawmillApplication {
	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			runApplication<SawmillApplication>(*args)
		}
	}
}
