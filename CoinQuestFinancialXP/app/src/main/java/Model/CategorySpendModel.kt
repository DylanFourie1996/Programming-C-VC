package Model

import android.media.audiofx.AudioEffect.Descriptor
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "category_spend",
    foreignKeys = [
        ForeignKey(
            entity = BudgetModel::class,
            parentColumns = ["id"],
            childColumns = ["budgetId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CategorySpendModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val budgetId: Int,
    val category: Int,
    val descriptor: String,
    val photoUri: String,
    val amountSpent: Float,
    val note: String
)