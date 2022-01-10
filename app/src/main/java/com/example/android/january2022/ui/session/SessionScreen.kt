package com.example.android.january2022.ui.session

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.january2022.db.entities.*
import com.example.android.january2022.ui.exercises.SubTitleText
import com.example.android.january2022.ui.exercises.TitleText
import com.example.android.january2022.utils.UiEvent
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat
import java.util.*


@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun SessionScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: SessionViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    var selectedSessionExercise by remember { mutableStateOf(-1L) }
    val session = viewModel.currentSession
    val allSets by viewModel.setsList.observeAsState(listOf())
    val sessionExercises: List<SessionExerciseWithExercise> by viewModel.getSessionExercisesForSession().observeAsState(
        listOf()
    )

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when(event) {
                is UiEvent.Navigate -> onNavigate(event)
                is UiEvent.ShowSnackbar -> {
                    val result = scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action
                    )
                    if(result == SnackbarResult.ActionPerformed) {
                        viewModel.onEvent(SessionEvent.RestoreRemovedSet)
                    }
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onEvent(SessionEvent.OnAddSessionExerciseClicked) },
                shape = RoundedCornerShape(50),
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(Icons.Filled.Add, "")
            }
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
        ) {
            Box(
                Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                if (session != null) {
                    SessionInfoThings(session)
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(3f)
            ) {
                items(items = sessionExercises) { sessionExercise ->

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
}

@Composable
fun SessionInfoThings(session: Session) {
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
        Row(verticalAlignment = Alignment.CenterVertically) {
            TitleText(text = startDate.lowercase(), bottomPadding = 2)
            Spacer(Modifier.weight(1f))
            //Text(session.sessionId.toString())
        }
        SubTitleText(text = "$startTime - $endTime", indent = 16)
        SubTitleText(text = session.trainingType)
    }
}