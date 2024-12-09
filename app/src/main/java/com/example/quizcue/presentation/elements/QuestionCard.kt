package com.example.quizcue.presentation.elements

import android.provider.MediaStore.Images
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.key.Key.Companion.I
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.example.quizcue.domain.model.Question

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun QuestionCard(
    question: Question,
    modifier: Modifier,
    iconColor: Color,
    onClick: () -> Unit,
    onAddToFavorites: (Question) -> Unit,
    onDeleteQuestion: (Question) -> Unit
) {
    val haptics = LocalHapticFeedback.current
    var isBottomSheetVisible: Boolean by remember { mutableStateOf(false) }

    OutlinedCard(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .combinedClickable(
                onClick = onClick,
                onLongClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    isBottomSheetVisible = true
                },
            ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
        colors = CardDefaults.cardColors(Color.Transparent)
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.Filled.QuestionMark,
                contentDescription = null,
                tint = iconColor
            )
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                text = question.text,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
    if (isBottomSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { isBottomSheetVisible = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(25.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Button(
                    onClick = {
                        onAddToFavorites(question)
                        isBottomSheetVisible = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (!question.isFavourite) {
                        Icon(
                            modifier = Modifier.padding(7.dp),
                            imageVector = Icons.Outlined.FavoriteBorder,
                            contentDescription = null
                        )
                        Text("Добавить в избранное")
                    } else {
                        Icon(
                            modifier = Modifier.padding(7.dp),
                            imageVector = Icons.Outlined.Favorite,
                            contentDescription = null
                        )
                        Text("Удалить из избранного")
                    }
                }

                Button(
                    onClick = {
                        onDeleteQuestion(question)
                        isBottomSheetVisible = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(modifier = Modifier.padding(7.dp),
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = null)
                    Text("Удалить")
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
