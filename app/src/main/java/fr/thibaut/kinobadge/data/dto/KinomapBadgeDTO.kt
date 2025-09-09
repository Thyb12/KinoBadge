package fr.thibaut.kinobadge.data.dto

import fr.thibaut.kinobadge.model.Badge
import fr.thibaut.kinobadge.model.Category
import fr.thibaut.kinobadge.model.ImagesUrl
import fr.thibaut.kinobadge.model.KinomapBadge
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KinomapBadgeDTO(
    val data: List<CategoryDTO> = emptyList(),
    @SerialName("unlocked_content") val unlockedContent: List<Unit> = emptyList() // on s'en fiche
)

@Serializable
data class CategoryDTO(
    val name: String,
    val badges: List<BadgeDTO> = emptyList()
)

@Serializable
data class BadgeDTO(
    val id: Int,
    val name: String,
    val description: String,
    val action: String,
    val category: String,
    @SerialName("unlocked_date") val unlockedDate: Long? = null,
    @SerialName("unlocked_percent") val unlockedPercent: Int? = null,
    @SerialName("images_url") val imagesUrl: ImagesUrlDTO
)

@Serializable
data class ImagesUrlDTO(
    val unlocked: String,
    val locked: String
)

fun KinomapBadgeDTO.toKinomapBadge(): KinomapBadge =
    KinomapBadge(
        data = data.map { cat ->
            Category(
                name = cat.name,
                badges = cat.badges.map { b ->
                    Badge(
                        id = b.id,
                        name = b.name,
                        description = b.description,
                        action = b.action,
                        category = b.category,
                        unlocked_date = b.unlockedDate,
                        unlocked_percent = b.unlockedPercent,
                        images_url = ImagesUrl(
                            unlocked = b.imagesUrl.unlocked,
                            locked = b.imagesUrl.locked
                        )
                    )
                }
            )
        }
    )
