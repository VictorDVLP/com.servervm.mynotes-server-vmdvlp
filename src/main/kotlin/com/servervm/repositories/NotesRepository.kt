package com.servervm.repositories

import com.servervm.database.NotesDatabase
import com.servervm.extensions.toNote
import com.servervm.models.Note
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import java.io.File

private const val DATABASE_NAME = "mynotesvm.db"

object NotesRepository {

    private val notesDb = JdbcSqliteDriver("jdbc:sqlite:$DATABASE_NAME").let {
        if (!File(DATABASE_NAME).exists()) {
            NotesDatabase.Schema.create(it)
        }
        NotesDatabase(it)
    }.notesQueries

    fun saveNote(note: Note): Note {
        notesDb.insert(note.title, note.description, note.type.name)
        return notesDb.selectLastInsertedNote().executeAsOne().toNote()
    }
//        note.copy(id = currentId++)
//            .also(list::add)

    fun getAllNotes(): List<Note> = notesDb.select().executeAsList().map { it.toNote() }

    fun getNoteById(id: Long): Note? = notesDb.selectById(id = id).executeAsOneOrNull()?.toNote()

    fun updateNote(note: Note): Boolean {
        if (getNoteById(note.id) == null) return false
        notesDb.update(
            id = note.id,
            title = note.title,
            description = note.description,
            type = note.type.name
        )
        return true
    }
//        =
//        list.indexOfFirst { it.id == note.id }
//            .takeIf { it >= 0 }
//            ?.also { list[it] = note }
//            .let { it != null }

    fun deleteNote(id: Long): Boolean {
        if (getNoteById(id) == null) return false
        notesDb.delete(id)
        return true
    }
//        =
//        list.indexOfFirst { it.id == id }
//            .takeIf { it >= 0 }
//            ?.also(list::removeAt)
//            .let { it != null }
}
