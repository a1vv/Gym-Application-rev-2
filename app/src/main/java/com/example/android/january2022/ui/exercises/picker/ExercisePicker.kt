package com.example.android.january2022.ui.exercises.picker

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.ui.exercises.ExerciseEvent
import com.example.android.january2022.ui.exercises.ExerciseViewModel
import com.example.android.january2022.ui.exercises.ExercisesList
import com.example.android.january2022.utils.Event
import com.example.android.january2022.utils.UiEvent
import com.google.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.flow.collect


@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ExercisePickerScreen(
    onPopBackStack: () -> Unit,
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: ExerciseViewModel = hiltViewModel()
) {
    val exercises: List<Exercise> by viewModel.exerciseList.collectAsState(listOf())
    val selectedExercises by viewModel.selectedExercises.collectAsState(initial = emptySet())

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.PopBackStack -> onPopBackStack()
                is UiEvent.Navigate -> onNavigate(event)
                else -> Unit
            }
        }
    }

    Scaffold(
        bottomBar = { BottomAppBar() {}},
        floatingActionButton = {
            // TODO: Animate fab entering and exiting screen when items get selected.
            ExtendedFloatingActionButton(
                onClick = { viewModel.onEvent(ExerciseEvent.AddExercisesToSession) },
                shape = RoundedCornerShape(35),
                containerColor = MaterialTheme.colorScheme.primary,
                text = { Text("ADD ${selectedExercises.size}") }
            )
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            TitleText("CHOOSE EXERCISE", 8, 16)
            ExerciseSearchFilters(viewModel, viewModel::onEvent)
            Spacer(Modifier.height(8.dp))
            Box(
                Modifier.weight(1f)
            ) {
                ExercisesList(viewModel, exercises, selectedExercises, viewModel::onEvent, true)
            }
        }
    }
}

@Composable
fun ExerciseSearchFilters(viewModel: ExerciseViewModel, onEvent: (Event) -> Unit) {
    val selectedMuscleGroups by viewModel.selectedMuscleGroups.collectAsState(emptyList())
    val muscleGroups by remember { mutableStateOf(viewModel.muscleGroups) }
    val selectedEquipment by viewModel.selectedEquipment.collectAsState("")
    val equipment by remember { mutableStateOf(viewModel.equipment)}

    LazyRow(contentPadding = PaddingValues(4.dp)) {
        items(muscleGroups) { muscleGroup ->
            val isSelected = selectedMuscleGroups.contains(muscleGroup)
            MuscleChip(
                title = muscleGroup,
                isSelected = isSelected,
                onEvent = {
                    onEvent(ExerciseEvent.MuscleGroupSelectionChange(it))
                }
            )
        }
    }
    LazyRow(contentPadding = PaddingValues(4.dp)) {
        items(equipment) { equipment ->
            val isSelected = selectedEquipment.contains(equipment)
            MuscleChip(
                title = equipment,
                isSelected = isSelected,
                onEvent = {
                    onEvent(ExerciseEvent.EquipmentSelectionChange(it))
                }
            )

        }
    }
}

@Composable
fun MuscleChip(
    title: String,
    isSelected: Boolean,
    onEvent: (String) -> Unit
) {
    val chipColor = animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    )
    Surface(
        shape = RoundedCornerShape(50),
        color = chipColor.value,
        tonalElevation = 1.dp
    ) {
        Box(modifier = Modifier.toggleable(value = isSelected, onValueChange = {
            onEvent(title)
        })) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun TitleText(text: String, bottomPadding: Int = 0, startPadding: Int = 0) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.displayMedium,
        modifier = Modifier.padding(bottom = bottomPadding.dp, start = startPadding.dp)
    )
}

@Composable
fun SubTitleText(text: String, bottomPadding: Int = 0, indent: Int = 0) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.secondary,
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(bottom = bottomPadding.dp, start = indent.dp)
    )
}
