package com.servervm.models

import kotlinx.serialization.Serializable

@Serializable
data class Note( val id: Long, val title: String, val description: String, val type: TypeNotes) {
    enum class TypeNotes {
        WRITTEN_NOTE,
        AUDIO_NOTE
    }
}
