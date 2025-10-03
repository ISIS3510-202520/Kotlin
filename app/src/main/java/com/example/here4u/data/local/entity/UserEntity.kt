import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import java.util.*

@Entity(tableName = "Users")
data class UserEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "display_name") val displayName: String,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "last_login") val lastLogin: Long,
    @ColumnInfo(name = "current_streak") val currentStreak: Int,
    @ColumnInfo(name = "longest_streak") val longestStreak: Int,
    @ColumnInfo(name = "last_entry_date") val lastEntryDate: Long
)
