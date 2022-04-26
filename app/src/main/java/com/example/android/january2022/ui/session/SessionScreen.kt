package com.example.android.january2022.ui.session

import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.SnackbarResult.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.january2022.db.entities.*
import com.example.android.january2022.ui.exercises.picker.SubTitleText
import com.example.android.january2022.utils.UiEvent
import java.text.SimpleDateFormat
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: SessionViewModel = hiltViewModel()
) {
    val selectedSessionExercise = viewModel.selectedSessionExercise
    val session = viewModel.currentSession
    val allSets by viewModel.setsList.observeAsState(listOf())
    val sessionExercises: List<SessionExerciseWithExercise> by viewModel.getSessionExercisesForSession()
        .observeAsState(
            listOf()
        )
    val muscleGroups = session?.let {
        viewModel.getMuscleGroupsForSession(it.sessionId).collectAsState(initial = emptyList())
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val scrollBehavior = remember(decayAnimationSpec) {
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(decayAnimationSpec)
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigate(event)
                is UiEvent.ShowSnackbar -> {
                    val result = snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.actionLabel
                    )
                    if (result == ActionPerformed) {
                        if (event.action != null) viewModel.onEvent(event.action)
                    }
                }
                else -> Unit
            }
        }
    }
    Scaffold(
        modifier = Modifier
            .padding(bottom = 60.dp)
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .windowInsetsPadding(WindowInsets.statusBars),
        topBar = {
            LargeTopAppBar(
                title = { Text(text = session?.let { sessionTitle(it) } ?: "") },
                colors = TopAppBarDefaults.mediumTopAppBarColors(),
                actions = {
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "Localized description"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onEvent(SessionEvent.OnAddSessionExerciseClicked) },
                shape = RoundedCornerShape(35),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Filled.Add, "")
            }
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .fillMaxSize()
                //.statusBarsPadding()
        ) {
            item {
                Surface(
                    Modifier
                        .padding(16.dp)
                ) {
                    SessionInfoCard(session, muscleGroups)
                }
            }
            items(
                items = sessionExercises,
                key = { it.sessionExercise.sessionExerciseId }) { sessionExercise ->

                val sets = mutableListOf<GymSet>()
                allSets.forEach { set ->
                    if (set.parentSessionExerciseId == sessionExercise.sessionExercise.sessionExerciseId) {
                        sets.add(set)
                    }
                }
                SessionExerciseCard(
                    sessionExercise = sessionExercise,
                    viewModel = viewModel,
                    selected = selectedSessionExercise,
                    sets = sets,
                    onEvent = viewModel::onEvent
                )
            }
            item { Spacer(Modifier.height(100.dp)) }
        }
    }

}

fun sessionTitle(session: Session): String {
    return SimpleDateFormat(
        "MMM d yyyy",
        Locale.ENGLISH
    ).format(session.startTimeMilli)
}

@Composable
fun SessionInfoThings(session: Session, muscleGroups: State<List<String>>?) {
    val startDate = SimpleDateFormat(
        "MMM d yyyy",
        Locale.ENGLISH
    ).format(session.startTimeMilli)
    val startTime = SimpleDateFormat(
        "HH:mm",
        Locale.ENGLISH
    ).format(session.startTimeMilli)
    val endTime = SimpleDateFormat(
        "HH:mm",
        Locale.ENGLISH
    ).format(session.endTimeMilli)

    Column {
        SubTitleText(text = "$startTime - $endTime", indent = 16)
        SubTitleText(text = session.trainingType)
        Row {
            muscleGroups?.value?.filter { it.isNotEmpty() }?.forEach { s ->
                Text(text = s)
                Spacer(Modifier.width(4.dp))
            }
        }
    }
}