package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaytimeDao {

    @Query("SELECT * FROM playtime_record WHERE id = 1 LIMIT 1")
    fun getPlaytimeRecord(): Flow<PlaytimeRecord?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaytime(record: PlaytimeRecord)

    @Update
    suspend fun updatePlaytime(record: PlaytimeRecord)

    @Query("UPDATE playtime_record SET totalPlaytimeSeconds = :totalPlaytime, continuousSessionSeconds = :sessionSecs WHERE id = 1")
    suspend fun updateTimers(totalPlaytime: Long, sessionSecs: Long)

    @Query("UPDATE playtime_record SET soulCoins = soulCoins + :coinsEarned WHERE id = 1")
    suspend fun rewardSoulCoins(coinsEarned: Int)
}
