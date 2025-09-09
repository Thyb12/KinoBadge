package fr.thibaut.kinobadge.data.repository

import fr.thibaut.kinobadge.Result
import fr.thibaut.kinobadge.data.dto.KinomapBadgeDTO
import fr.thibaut.kinobadge.data.dto.toKinomapBadge
import fr.thibaut.kinobadge.model.KinomapBadge
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json

class KinomapServiceImpl : KinomapService {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        prettyPrint = false
    }

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) { json(this@KinomapServiceImpl.json) }
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun recupererBadges(
        onResult: (Result<List<KinomapBadge>>) -> Unit
    ) {
        scope.launch {
            try {
                val resp: HttpResponse =
                    client.get("https://api.kinomap.dev/v4/badges/mobile-tech-test?appToken=Y7pNWqI4nlYuGBILm46tqw57aKInntGTpzQau30To8WDSt6ZOU60GHWG8QSyWIs1TsFrnheftxBmmFWxR4eKhUWruEndo0aXaZVC6tn9fWhdBDb0ThVvmY6E")

                val dto = json.decodeFromString<KinomapBadgeDTO>(resp.bodyAsText())
                val domain: KinomapBadge = dto.toKinomapBadge()

                withContext(Dispatchers.Main) {
                    onResult(Result(success = listOf(domain)))
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onResult(Result(error = e.message ?: "Unknown error"))
                }
            }
        }
    }
}
