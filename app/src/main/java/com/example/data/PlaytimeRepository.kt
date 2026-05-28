package com.example.data

import kotlinx.coroutines.flow.Flow

class PlaytimeRepository(private val playtimeDao: PlaytimeDao) {

    val playtimeRecord: Flow<PlaytimeRecord?> = playtimeDao.getPlaytimeRecord()

    suspend fun saveRecord(record: PlaytimeRecord) {
        playtimeDao.insertPlaytime(record)
    }

    suspend fun updateTimers(totalPlaytime: Long, sessionSecs: Long) {
        playtimeDao.updateTimers(totalPlaytime, sessionSecs)
    }

    suspend fun addSoulCoins(coins: Int) {
        playtimeDao.rewardSoulCoins(coins)
    }
}
