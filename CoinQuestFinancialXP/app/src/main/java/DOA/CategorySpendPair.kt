package DOA

import Model.CategoryModel
import androidx.room.Embedded

data class CategorySpendPair(
    @Embedded val category : CategoryModel,
    val spend : Float
)