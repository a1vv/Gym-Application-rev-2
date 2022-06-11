package com.example.android.january2022.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime


/**
 * A workout-session contains multiple SessionExercises. Has a start and end-time.
 */
@Entity(tableName = "sessions")
data class Session(
    @PrimaryKey(autoGenerate = true)
    var sessionId: Long = 0L,

    @ColumnInfo(name = "start")
    val start: LocalDateTime = LocalDateTime.now(),

    @ColumnInfo(name = "end")
    var end: LocalDateTime = start,

    @ColumnInfo(name = "training_type")
    var trainingType: String = "",

    )