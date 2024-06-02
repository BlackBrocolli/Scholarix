package id.ac.stiki.doleno.scholarix.model

data class Beasiswa(
    var id: String = "",
    val link: String = "",
    val name: String = "",
    val fundingStatus: String? = null,
    val deadline: String? = "N/A",
    val degrees: List<String> = listOf(""),
    val city: String? = null,
    val country: String = "",
    val institution: String = "",
    val opportunities: String = "N/A",
    val eligibleNationals: String = "Tidak ada keterangan",
    val duration: String = "N/A",
    val languageRequirements: String = "N/A",
    val documentsHtml: List<String> = listOf(""),
    val benefitsHtml: List<String> = listOf(""),
    val email: String? = null,
    val phone: String? = null,
    val description: String = "",
    val otherCriteria: String = "",
    val amount: String = ""
)

object DummyBeasiswa {
    val beasiswaList = listOf(
        Beasiswa(
            id = "0",
            link = "",
            name = "Beasiswa ABC INIlah beasiswa dengan nama panjang",
            fundingStatus = "Fully Funded",
            degrees = listOf("S1", "S2", "S3"),
            city = "Jakata",
            country = "Indonesia",
            institution = "Colombia Educational Institutes",
            eligibleNationals = "Anda harus lebih tua dari 50 tahun",
            documentsHtml = listOf(
                "Recommendation letter (Academic)",
                "Curriculum Vitae (CV)",
                "Spanish Proficiency proof",
                "Copy of Passport"
            ),
            benefitsHtml = listOf(
                "Full tuition fee coverage (only for academic programs found in the catalog of this call)",
                "Grant of the sum equivalent to 3 Minimum Monthly Legal salaries. \$ 2,484,348 Colombian pesos.",
                "A one-time stipend for books and materials at the first of the programs: \$401.321 COP",
                "Coverage of Medical Insurance during studies in Colombia."
            ),
            duration = "2 years or more",
            opportunities = "103 scholarships",
            languageRequirements = "Varies",
            email = null,
            phone = ""
        ),
        Beasiswa(
            id = "1",
            link = "",
            name = "Beasiswa XYZ",
            fundingStatus = "Partial Funded",
            deadline = "May 15, 2024",
            degrees = listOf("S2", "S3"),
            city = "Jakata",
            country = "Indonesia",
            institution = "Colombia Educational Institutes",
            opportunities = "120",
            documentsHtml = listOf(
                "Recommendation letter (Academic)",
                "Curriculum Vitae (CV)",
                "Spanish Proficiency proof",
                "Copy of Passport"
            ),
            benefitsHtml = listOf(
                "Full tuition fee coverage (only for academic programs found in the catalog of this call)",
                "Grant of the sum equivalent to 3 Minimum Monthly Legal salaries. \$ 2,484,348 Colombian pesos.",
                "A one-time stipend for books and materials at the first of the programs: \$401.321 COP",
                "Coverage of Medical Insurance during studies in Colombia."
            ),
            languageRequirements = "TOEFL, IELTS",
            email = null,
            phone = ""
        ),
        Beasiswa(
            id = "2",
            name = "Beasiswa 123",
            fundingStatus = "Fully Funded",
            deadline = "June 1, 2024",
            degrees = listOf("S1", "S2"),
            city = "Jakata",
            country = "Indonesia",
            institution = "Colombia Educational Institutes",
            documentsHtml = listOf(
                "Recommendation letter (Academic)",
                "Curriculum Vitae (CV)",
                "Spanish Proficiency proof",
                "Copy of Passport"
            ),
            benefitsHtml = listOf(
                "Full tuition fee coverage (only for academic programs found in the catalog of this call)",
                "Grant of the sum equivalent to 3 Minimum Monthly Legal salaries. \$ 2,484,348 Colombian pesos.",
                "A one-time stipend for books and materials at the first of the programs: \$401.321 COP",
                "Coverage of Medical Insurance during studies in Colombia."
            ),
            email = null,
            phone = "",
            link = "",
        )
    )
}