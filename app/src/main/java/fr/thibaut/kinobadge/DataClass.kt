package fr.thibaut.kinobadge

data class ReponseApi(
    val data: List<Category>,
    val unlocked_content: List<Any>
)

data class Category(
    val name: String,
    val badges: List<Badge>
)

data class Badge(
    val id: Int,
    val name: String,
    val description: String,
    val action: String,
    val category: String,
    val unlocked_date: Long?,
    val unlocked_percent: Int?,
    val images_url: ImagesUrl
)

data class ImagesUrl(
    val unlocked: String,
    val locked: String
)