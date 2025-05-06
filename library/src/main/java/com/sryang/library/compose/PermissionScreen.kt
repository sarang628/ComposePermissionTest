package com.sryang.library.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState

/**
 * @param state 권한을 요청할 수 있는 객체
 * @param description 권한에 대해 설명하고 싶다면 작성
 * @param errorText 권한 거절 시 나오는 에러 메세지
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun PermissionScreen(state: MultiplePermissionsState, description: String?, errorText: String) {
    var showRationale by remember(state) { mutableStateOf(false) } // Rationale: 이유[근거] 처음 한 번 거절하면 두 번째 재요청 시 true로 변함. 두 번째도 거절하면 더 이상 권한 요청 안됨.
    val permissions = remember(state.revokedPermissions) { state.revokedPermissions.joinToString("\n") { " - " + it.permission.removePrefix("android.permission.") } }
    Column {
        Button(onClick = { if (state.shouldShowRationale) { showRationale = true } else { state.launchMultiplePermissionRequest() } }) {
            Text(text = "MyLocation")
        }
        if (errorText.isNotBlank()) {
            Text(text = errorText, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(16.dp))
        }
        description?.let {
            if(it.isNotBlank()){
                Text(text = it, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(16.dp))
            }
        }
    }
    if (showRationale) {
        AlertDialog(
            onDismissRequest = { showRationale = false },
            title = { Text(text = "Permissions required by the sample") },
            text = { Text(text = "The sample requires the following permissions to work:\n $permissions") },
            confirmButton = { TextButton(onClick = { showRationale = false; state.launchMultiplePermissionRequest() }) { Text("Continue") } },
            dismissButton = { TextButton(onClick = { showRationale = false }) { Text("Dismiss") } },
        )
    }
}
