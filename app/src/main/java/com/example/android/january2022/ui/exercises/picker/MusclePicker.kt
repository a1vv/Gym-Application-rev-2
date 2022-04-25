package com.example.android.january2022.ui.exercises.picker

import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.january2022.db.MuscleItem
import com.example.android.january2022.ui.exercises.ExerciseEvent
import com.example.android.january2022.ui.exercises.ExerciseViewModel
import com.example.android.january2022.utils.UiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusclePickerScreen(
    onPopBackStack: () -> Unit,
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: ExerciseViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.PopBackStack -> onPopBackStack()
                is UiEvent.Navigate -> onNavigate(event)
                else -> Unit
            }
        }
    }
    val muscleGroups = viewModel.muscleGroups
    val selectedExercises by viewModel.selectedExercises.collectAsState(initial = emptySet())
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val scrollBehavior = remember(decayAnimationSpec) {
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(decayAnimationSpec)
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(text = "Exercises") },
                colors = TopAppBarDefaults.mediumTopAppBarColors(),
                actions = {
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Localized description"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            // TODO: Animate fab entering and exiting screen when items get selected.
            ExtendedFloatingActionButton(
                onClick = { viewModel.onEvent(ExerciseEvent.AddExercisesToSession) },
                shape = RoundedCornerShape(35),
                containerColor = MaterialTheme.colorScheme.primary
            ) { Text("ADD ${selectedExercises.size}") }
        },
        floatingActionButtonPosition = FabPosition.End,
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(bottom = 60.dp)
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            userScrollEnabled = false,
            verticalArrangement = Arrangement.Top,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(
                    top = innerPadding.calculateTopPadding()
                )
        ) {
            items(
                count = muscleGroups.size,
            ) { index ->
                val muscleGroup = muscleGroups[index]
                MuscleItem(
                    muscleGroup = muscleGroup,
                    selectedExercises = selectedExercises
                ) { viewModel.onEvent(ExerciseEvent.OnMuscleGroupSelected(muscleGroup)) }
            }
        }
    }
}