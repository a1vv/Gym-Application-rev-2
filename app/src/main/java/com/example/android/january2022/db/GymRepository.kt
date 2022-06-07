package com.example.android.january2022.db

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.android.january2022.db.entities.*
import com.example.android.january2022.utils.turnTargetIntoMuscleGroup
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class GymRepository(
    private val dao: GymDAO
) {

    suspend fun updateSession(session: Session) = dao.updateSession(session)

    fun getLastExercise() =
        dao.getLastExercise()

    fun getExercise(id: Long) =
        dao.getExercise(id)

    suspend fun getExercisesByQuery(
        muscleGroup: String = "%",
        equipment: String = "%",
        query: String = "%",
    ): Flow<List<Exercise>> {
        val muscle = "%${muscleGroup}%"
        val equip = "%${equipment}%"
        val que = "%${query}%"

        return flow {
            dao.getExercisesByQuery(equip, que).collect {
                emit(it.filter { exercise ->
                    var match = false
                    exercise.targets.map { turnTargetIntoMuscleGroup(it) }.forEach {
                        if (!it.lowercase().contains(muscleGroup.lowercase())) return@forEach
                        match = true
                    }
                    Log.d("Repository", "exercise ${exercise.getMuscleGroup()} match $match")
                    match
                })
            }
        }
    }

    fun getSession(id: Long) =
        dao.getSession(id)

    fun getSessions() =
        dao.getAllSessions()

    fun getAllExercises() =
        dao.getAllExercises()

    fun getSets() =
        dao.getAllSets()

    fun getSessionExercises() =
        dao.getSessionExercisesWithExercise()

    fun getSessionExercisesForSession(sessionId: Long): LiveData<List<SessionExerciseWithExercise>> =
        dao.getSessionExercisesWithExerciseForSession(sessionId)

    fun getMuscleGroupsForSession(sessionId: Long): List<String> {
        val muscleGroups = mutableListOf<String>()
        dao.getSessionMuscleGroups(sessionId).forEach {
            turnTargetIntoMuscleGroup(it).split(", ").forEach { muscleGroups.add(it) }
        }
        val muscleGroupsByCount = muscleGroups.groupingBy { it }.eachCount()
        return muscleGroupsByCount.entries.sortedBy { it.value }.map { it.key }.reversed()
    }

    fun getSetsForSessionExercise(id: Long) =
        dao.getSetsForSessionExercise(id)

    fun getSetsForSession(id: Long) =
        dao.getSetsForSession(id)

    suspend fun updateSet(set: GymSet) =
        dao.updateSet(set)

    suspend fun insertSet(item: GymSet) =
        dao.insertSet(item)

    suspend fun insertSession(item: Session): Long =
        dao.insertSession(item)

    suspend fun insertExercise(exercise: Exercise) =
        dao.insertExercise(exercise)

    suspend fun insertSessionExercise(sessionExercise: SessionExercise) =
        dao.insertSessionExercise(sessionExercise)

    suspend fun removeSession(session: Session) =
        dao.removeSession(session)

    suspend fun removeSet(set: GymSet) =
        dao.removeSet(set)

    suspend fun removeSessionExercise(sessionExercise: SessionExercise) =
        dao.removeSessionExercise(sessionExercise)

}
