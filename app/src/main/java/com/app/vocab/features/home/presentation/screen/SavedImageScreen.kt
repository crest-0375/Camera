package com.app.vocab.features.home.presentation.screen

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.rememberAsyncImagePainter
import com.app.vocab.features.home.domain.state.SaveImageState
import com.app.vocab.features.home.presentation.viewmodel.HomeViewModel

@Composable
fun SavedImageScreen(
    innerPadding: PaddingValues,
    uri: Uri,
    onBackOrFinish: () -> Unit,
    onSaveClick: () -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val saveImageState by homeViewModel.saveImageState.collectAsState()
    var isShowLoading by remember { mutableStateOf(false) }

    LaunchedEffect(saveImageState) {
        isShowLoading = saveImageState is SaveImageState.Loading
    }

    if (isShowLoading) {
        Dialog(
            onDismissRequest = { isShowLoading = false },
            properties = DialogProperties(
                dismissOnClickOutside = false,
                dismissOnBackPress = false
            ),
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clip(RoundedCornerShape(10))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(40.dp)
            )
        }
    }

    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(top = 20.dp)
            .padding(horizontal = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onTertiary),
                onClick = onBackOrFinish

            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back Icon"
                )
            }

            Text(
                text = "Apply colors",
                fontSize = 18.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            IconButton(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onTertiary),
                onClick = onSaveClick

            ) {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = "Edit Icon",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Image(
            painter = rememberAsyncImagePainter(model = uri),
            contentDescription = "Captured Image",
            modifier = Modifier.padding(top = 20.dp)
        )
        Button(onClick = { homeViewModel.saveImageToStorage(uri) }) {
            Text(text = "Save Image")
        }
    }
}