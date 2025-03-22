package community.whatever.onembackendkotlin.presentation

import io.micrometer.observation.Observation
import io.micrometer.observation.ObservationHandler
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.stream.StreamSupport

@Component
class CustomObservationHandler : ObservationHandler<Observation.Context> {

    private val log = LoggerFactory.getLogger(CustomObservationHandler::class.java)

    override fun onStart(context: Observation.Context) {
        log.info(
            "Before running the observation for context [{}], userType [{}]",
            context.name,
            getUserTypeFromContext(context)
        );
    }

    override fun onStop(context: Observation.Context) {
        log.info(
            "After running the observation for context [{}], userType [{}]",
            context.name,
            getUserTypeFromContext(context)
        );
    }

    override fun supportsContext(p0: Observation.Context): Boolean {
        return true
    }

    private fun getUserTypeFromContext(context: Observation.Context): String {
        return StreamSupport.stream(context.lowCardinalityKeyValues.spliterator(), false)
            .filter { it.key == "userType" }
            .findFirst()
            .map { it.value }
            .orElse("unknown")
    }
}
