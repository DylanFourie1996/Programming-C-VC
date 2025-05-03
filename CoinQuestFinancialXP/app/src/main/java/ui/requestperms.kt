package ui

import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun RequestImagePermissionIfNeeded() {
    // (Developer, 2025)
    val context = LocalContext.current
    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        android.Manifest.permission.READ_MEDIA_IMAGES
    } else {
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    }

    // (Developer et al., 2025)
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (!isGranted) {
                Toast.makeText(context, "Permission needed to load images", Toast.LENGTH_LONG).show()
            }
        }
    )

    // (Developer et al., 2025)
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            launcher.launch(permission)
        }
    }
}

/*
References

Developers. 2025. Access media files from shared storage, 16 April 2025. [Online]. Available at: https://developer.android.com/training/data-storage/shared/media [Accessed 3 May 2025].
Developers. 2025. Compose and other libraries, 16 April 2025. [Online]. Available at: https://developer.android.com/develop/ui/compose/libraries [Accessed 3 May 2025].
 */