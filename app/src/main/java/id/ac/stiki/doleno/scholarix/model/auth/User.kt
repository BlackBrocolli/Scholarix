package id.ac.stiki.doleno.scholarix.model.auth

data class User(
    val namaLengkap: String = "",
    val email: String = "",
    val noHandphone: String = "",
    val profilePictureUrl: String = "",
    val favorites: List<String> = listOf(),
    val preference: Preference = Preference()
)

data class Preference(
    val degrees: List<String> = listOf(),
    val fundingStatus: List<String> = listOf(),
    val countries: List<String> = listOf()
)
