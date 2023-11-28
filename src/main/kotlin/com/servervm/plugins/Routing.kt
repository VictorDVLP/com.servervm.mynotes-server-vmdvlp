package com.servervm.plugins

import com.servervm.models.Note
import com.servervm.repositories.NotesRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        route("/notes") {

            //CREATE
            post {
                try {
                    val note = call.receive<Note>()
                    val newNote = NotesRepository.saveNote(note)
                    call.respond(HttpStatusCode.Created, newNote)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, "Bad JSON Data Body: ${e.message}")
                }

            }

            //READ
            get {
                call.respond(NotesRepository.getAllNotes())
            }

            get("/{id}") {
                val id = call.parameters["id"] ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    "Missing or malformed id"
                )
                val note = NotesRepository.getNoteById(id.toLong()) ?: return@get call.respond(
                    HttpStatusCode.NotFound, "Note not found with id $id"
                )
                call.respond(note)
            }

            //UPDATE
            put {
                try {
                    val note = call.receive<Note>()
                    if (NotesRepository.updateNote(note)) {
                        call.respond(note)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Note not found with id ${note.id}")
                    }

                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, "Bad JSON Data Body: ${e.message}")
                }
            }

            //DELETE
            delete("{id}") {
                val id = call.parameters["id"] ?: return@delete call.respond(
                    HttpStatusCode.BadRequest, "Missing or malformed id"
                )
                if (NotesRepository.deleteNote(id.toLong())) {
                    call.respond(HttpStatusCode.Accepted)
                } else {
                    call.respond(HttpStatusCode.NotFound, "Note not found with id $id")
                }

            }
        }
    }
}

