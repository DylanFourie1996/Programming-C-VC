package Model

fun BudgetModel.getDurationTypeAsText(): String {
    return when (durationType) {
        1 -> "Weekly"
        2 -> "Biweekly"
        3 -> "Monthly"
        else -> "Unknown"
    }
}
