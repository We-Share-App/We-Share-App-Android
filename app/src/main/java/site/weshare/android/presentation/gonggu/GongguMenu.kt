package site.weshare.android.presentation.gonggu.components

import site.weshare.android.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ==================== More Menu Component ====================
@Composable
fun GongguMoreMenu(
    onMenuAction: (String) -> Unit = {}
) {
    val isPreview = LocalInspectionMode.current
    var menuExpanded by remember { mutableStateOf(isPreview) }

    Box {
        IconButton(onClick = { menuExpanded = true }) {
            Icon(Icons.Default.MoreVert, contentDescription = "더보기")
        }

        DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false },
            shape = RoundedCornerShape(0.dp),
            shadowElevation = 5.dp,
            tonalElevation = 0.dp,
            containerColor = Color.White,
            modifier = Modifier.padding(0.dp)
        ) {
            GongguMenuItem(
                iconRes = R.drawable.report,
                text = "신고하기",
                onClick = {
                    menuExpanded = false
                    onMenuAction("report")
                }
            )

            GongguMenuItem(
                iconRes = R.drawable.share,
                text = "공유하기",
                onClick = {
                    menuExpanded = false
                    onMenuAction("share")
                }
            )

            GongguMenuItem(
                iconRes = R.drawable.ask,
                text = "문의하기",
                onClick = {
                    menuExpanded = false
                    onMenuAction("inquiry")
                }
            )
        }
    }
}

@Composable
private fun GongguMenuItem(
    iconRes: Int,
    text: String,
    onClick: () -> Unit
) {
    DropdownMenuItem(
        text = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Text(text, fontSize = 14.sp)
            }
        },
        onClick = onClick,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
        modifier = Modifier.height(30.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun GongguMoreMenuPreview() {
    Box {
        DropdownMenu(
            expanded = true,
            onDismissRequest = { },
            shape = RoundedCornerShape(0.dp),
            shadowElevation = 5.dp,
            tonalElevation = 0.dp,
            containerColor = Color.White,
            modifier = Modifier.padding(0.dp)
        ) {
            GongguMenuItem(
                iconRes = R.drawable.report,
                text = "신고하기",
                onClick = { }
            )

            GongguMenuItem(
                iconRes = R.drawable.share,
                text = "공유하기",
                onClick = { }
            )

            GongguMenuItem(
                iconRes = R.drawable.ask,
                text = "문의하기",
                onClick = { }
            )
        }
    }
}