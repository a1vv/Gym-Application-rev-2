package com.example.android.january2022.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.january2022.db.Equipment
import com.example.android.january2022.db.MuscleGroup


@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    @ColumnInfo(name = "title")
    var title: String = "Exercise",
    var force: String = "",
    @ColumnInfo(defaultValue = Equipment.NULL)
    var equipment: String = Equipment.NULL,
    var targets: List<String> = emptyList(),
    var synergists: List<String> = emptyList(),
    var url: String = "https://www.musclewiki.com"
)