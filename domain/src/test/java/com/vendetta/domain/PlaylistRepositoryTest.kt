package com.vendetta.domain

import com.vendetta.domain.entity.SongEntity
import com.vendetta.domain.repository.PlaylistRepository
import com.vendetta.domain.usecase.AddSongUseCase
import com.vendetta.domain.usecase.ChangeLikeStatusUseCase
import com.vendetta.domain.usecase.DeleteSongUseCase
import com.vendetta.domain.usecase.GetFavouriteSongsUseCase
import com.vendetta.domain.usecase.GetSongsUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class PlaylistRepositoryTest {

    private val repository = mockk<PlaylistRepository>()

    @Test
    fun `should return list of song in repository`() {
        val useCase = GetSongsUseCase(repository)
        every { repository.songs } returns MutableStateFlow(listOf())
        assertEquals(repository.songs, useCase())
    }

    @Test
    fun `should return list of favourite songs in repository`() {
        val useCase = GetFavouriteSongsUseCase(repository)
        every { repository.favouriteSongs } returns MutableStateFlow(listOf())
        assertSame(repository.favouriteSongs, useCase())
    }

    @Test
    fun `should delete song in repository`() = runTest {
        val song = mockSong(1)
        val useCase = DeleteSongUseCase(repository)
        coEvery { repository.deleteSong(song) } just Runs
        useCase(song)
        coVerify { repository.deleteSong(song) }
    }

    @Test
    fun `should add song in repository`() = runTest {
        val song = mockSong(2)
        val useCase = AddSongUseCase(repository)
        coEvery { repository.addSong(song.uri) } just Runs
        useCase(song.uri)
        coVerify { repository.addSong(song.uri) }
    }

    @Test
    fun `should change like status`() = runTest {
        val song = mockSong(3)
        val useCase = ChangeLikeStatusUseCase(repository)
        coEvery { repository.changeLikeStatus(song) } just Runs
        useCase(song)
        coVerify { repository.changeLikeStatus(song) }
    }


    private fun mockSong(idParam: Int, uriParam: String = ""): SongEntity {
        return mockk {
            every { this@mockk.id } returns idParam
            every { this@mockk == any<SongEntity>() } answers { (firstArg() as SongEntity).id == idParam }
            every { this@mockk.uri } returns uriParam
            every { this@mockk == any<SongEntity>() } answers { (secondArg() as SongEntity).uri == uriParam }
        }
    }
}