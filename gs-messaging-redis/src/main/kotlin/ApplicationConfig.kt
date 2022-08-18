import core.redis.RedisConfig
import io.dropwizard.Configuration
import org.jetbrains.annotations.NotNull
import javax.validation.Valid

data class ApplicationConfig(@Valid @NotNull val redis: RedisConfig) : Configuration()
