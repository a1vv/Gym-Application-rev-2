package com.example.android.january2022.ui.exercises

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.ui.exercises.picker.ExerciseSearchFilters
import com.example.android.january2022.utils.Event


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExercisesList(
    viewModel: ExerciseViewModel,
    exercises: List<Exercise>,
    selectedExercises: Set<Long> = emptySet(),
    onEvent: (Event) -> Unit,
    inPicker: Boolean,
    innerPadding: PaddingValues = PaddingValues(0.dp)
) {

    LazyColumn(
        contentPadding = innerPadding,
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            ExerciseSearchFilters(viewModel, viewModel::onEvent)
        }
        items(items = exercises) { exercise ->
            AnimatedVisibility(visible = true) {
                val selected = selectedExercises.contains(exercise.exerciseId)
                ExerciseCard(exercise, selected, inPicker, viewModel::onEvent)
            }
        }
    }
}