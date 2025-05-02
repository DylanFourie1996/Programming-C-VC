package Model
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.coinquest.data.UserModel

@Entity(
    tableName = "user_achievements",
    foreignKeys = [
        ForeignKey(
            entity = UserModel::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AchievementModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val achievementOneId: Int,
)
