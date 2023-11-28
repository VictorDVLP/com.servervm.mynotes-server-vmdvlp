package com.servervm.extensions

import com.servervm.database.DbNote
import com.servervm.models.Note

fun DbNote.toNote(): Note = Note(
    id = id,
    title = title,
    description = description,
    type = Note.TypeNotes.valueOf(type)
)