package id.ac.stiki.doleno.scholarix

data class Country(
    val name: Name,
    val flags: Flags
)

data class Name(
    val common: String
)

data class Flags(
    val png: String
)

data class CountriesResponse(val countries: List<Country>)