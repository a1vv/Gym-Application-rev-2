package com.example.android.january2022.db

import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.db.entities.GymSet
import com.example.android.january2022.db.entities.SessionExercise
import com.example.android.january2022.db.entities.Session


class GymRepository(
    private val dao: GymDAO
) {

    fun getLastExercise() =
        dao.getLastExercise()

    fun getSession(id: Long) =
        dao.getSession(id)

    fun getSessions() =
        dao.getAllSessions()

    fun getExercises() =
        dao.getAllExercises()

    fun getSets() =
        dao.getAllSets()

    fun getSessionExercises() =
        dao.getSessionExercisesWithExercise()

    fun getSessionExercisesForSession(sessionId: Long) =
        dao.getSessionExercisesWithExerciseForSession(sessionId)

    fun getSetsForSessionExercise(id: Long) =
        dao.getSetsForSessionExercise(id)

    fun getSetsForSession(id: Long) =
        dao.getSetsForSession(id)

    suspend fun updateSet(set: GymSet) =
        dao.updateSet(set)

    suspend fun insertSet(item: GymSet) =
        dao.insertSet(item)

    suspend fun insertSession(item: Session) : Long =
        dao.insertSession(item)

    suspend fun insertExercise(exercise: Exercise) =
        dao.insertExercise(exercise)

    suspend fun insertSessionExercise(sessionExercise: SessionExercise) =
        dao.insertSessionExercise(sessionExercise)

    suspend fun removeSet(set: GymSet) =
        dao.removeSet(set)

}
