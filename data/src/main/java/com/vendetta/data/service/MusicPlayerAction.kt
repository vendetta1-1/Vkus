package com.vendetta.data.service

sealed class MusicPlayerAction(
    val title: String
) {
    data object Play : MusicPlayerAction(PLAY_TITLE)
    data object Previous : MusicPlayerAction(PREVIOUS_TITLE)
    data object Next : MusicPlayerAction(NEXT_TITLE)
    data object ResumeOrPause : MusicPlayerAction(RESUME_OR_PAUSE_TITLE)

    private companion object {
        const val PLAY_TITLE = "play"
        const val PREVIOUS_TITLE = "previous"
        const val NEXT_TITLE = "next"
        const val RESUME_OR_PAUSE_TITLE = "resume_or_pause"
    }
}