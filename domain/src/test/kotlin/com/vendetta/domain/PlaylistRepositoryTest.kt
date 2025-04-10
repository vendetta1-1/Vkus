package com.vendetta.domain

import com.vendetta.domain.entity.SongEntity
import com.vendetta.domain.repository.PlaylistRepository
import com.vendetta.domain.usecase.AddSongUseCase
import com.vendetta.domain.usecase.ChangeLikeStatusUseCase
import com.vendetta.domain.usecase.DeleteSongUseCase
import com.vendetta.domain.usecase.GetFavouriteSongsUseCase
import com.vendetta.domain.usecase.GetSongsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class PlaylistRepositoryTest {

    private val repository = mock<PlaylistRepository>()

    private val testSong = SongEntity(
        id = 1,
        uri = "content://test-song",
        isFavourite = true,
        durationInMillis = 22,
        coverBitmap = ByteArray(2),
        songName = "Untitled",
        artistName = "Unknown",
        albumName = "Untitled"
    )

    @Test
    fun `changeLikeStatus should invoke repository method`() = runTest {
        val useCase = ChangeLikeStatusUseCase(repository)
        useCase(testSong)
        verify(repository).changeLikeStatus(testSong)
    }

    @Test
    fun `addSong should return song from repository`() = runTest {
        val testUri = "content://test-song"
        Mockito.`when`(repository.addSong(testUri)).thenReturn(testSong)
        val useCase = AddSongUseCase(repository)
        val actual = useCase(testUri)
        val expected = repository.addSong(testUri)
        assertEquals(expected, actual)
    }

    @Test
    fun `deleteSong should invoke repository method`() = runTest {
        val useCase = DeleteSongUseCase(repository)
        useCase(testSong)
        verify(repository).deleteSong(testSong)
    }

    @Test
    fun `get songs should return flow from repository`() = runTest {
        val testSongs = listOf(testSong)
        val testFlow = MutableStateFlow(testSongs)
        whenever(repository.songs).thenReturn(testFlow)

        val useCase = GetSongsUseCase(repository)
        val result = useCase().value

        assertEquals(testSongs, result)
    }

    @Test
    fun `observeFavourites should return flow from repository`() = runTest {
        val testFavourites = listOf(testSong.copy(isFavourite = true))
        val testFlow = MutableStateFlow(testFavourites)
        whenever(repository.favouriteSongs).thenReturn(testFlow)

        val useCase = GetFavouriteSongsUseCase(repository)
        val result = useCase().value

        assertEquals(testFavourites, result)
    }
}