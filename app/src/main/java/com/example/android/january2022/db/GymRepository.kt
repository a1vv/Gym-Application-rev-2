package com.example.android.january2022.db

import android.app.Application
import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.db.entities.SessionExercise
import com.example.android.january2022.db.entities.Session


class GymRepository(
    application: Application
) {    private var db: GymDatabase

    init {
        val database = GymDatabase.getInstance(application)
        db = database
    }

    fun getAllSessions() =
        db.gymDatabaseDAO.getAllSessions()

    suspend fun deleteAllSessions() =
        db.gymDatabaseDAO.clearSessions()

    suspend fun insertSession(item: Session) : Long =
        db.gymDatabaseDAO.insertSession(item)
}
