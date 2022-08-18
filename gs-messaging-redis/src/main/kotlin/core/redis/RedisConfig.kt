package core.redis

import javax.validation.constraints.NotEmpty

data class RedisConfig(@NotEmpty val uri: String)
