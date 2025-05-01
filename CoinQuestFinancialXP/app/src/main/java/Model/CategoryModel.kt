package Model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.coinquest.data.UserModel

/*
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
 */

@Entity(tableName = "category",
    foreignKeys = [
        ForeignKey(
            entity = UserModel::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE,
        )
    ])
data class CategoryModel(
    @PrimaryKey(autoGenerate=true) val id : Int = 0,
    val userId : Int,
    val title : String,
    val premade : Boolean
)