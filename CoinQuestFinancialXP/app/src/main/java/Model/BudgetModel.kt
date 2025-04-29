package Model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.coinquest.data.UserModel

@Entity(tableName = "budget",
    foreignKeys = [
        ForeignKey(
            entity = UserModel::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class BudgetModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val limit: Float,
    val spend: Float,
    val durationType: Int,
    val timePeriodType: String,
    val periodKey: String
)