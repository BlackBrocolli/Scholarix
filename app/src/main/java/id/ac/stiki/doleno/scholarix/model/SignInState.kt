package id.ac.stiki.doleno.scholarix.model

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInErrorMessage: String? = null
)
