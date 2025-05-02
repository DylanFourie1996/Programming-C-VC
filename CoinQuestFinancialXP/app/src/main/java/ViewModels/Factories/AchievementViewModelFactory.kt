//package ViewModels.Factories
//
//import DOA.AchievementDoa
//import DOA.BudgetDao
//import ViewModels.CaptureNewBudgetViewModel
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//
//class AchievementViewModelFactory(private val AchievementDoa: AchievementDoa) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(CaptureNewBudgetViewModel::class.java)) {
//            return CaptureNewBudgetViewModel(AchievementDoa as BudgetDao) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}
