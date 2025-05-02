package Model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(
    tableName = "categoryspend",
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
    val ItemName:String,
    val category: Int,
    val spend: Float,
    val photoUri: String,
)
// Date Created, If user want specific date they can choose date to end date. to filter it out.
