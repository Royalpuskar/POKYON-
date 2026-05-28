package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playtime_record")
data class PlaytimeRecord(
    @PrimaryKey val id: Int = 1, // Single record pattern for global app state
    val totalPlaytimeSeconds: Long = 0L,
    val continuousSessionSeconds: Long = 0L,
    val lastSyncTimestamp: Long = System.currentTimeMillis(),
    val soulCoins: Int = 500, // Starting currency
    val mantras: Int = 50, // Starting premium currency
    val ownerName: String = "ROYAL PUSKAR KHAS",
    val activeAvatarId: Int = 0, // 0: Tenzin, 1: Zoya, 2: Kaito, 3: Freyja, 4: Arjun
    val activeMapId: Int = 0, // 0: Sky Temple, 1: Crimson Dune, 2: Sakura Neon, 3: Nordic Frost, 4: Emerald Rain
    val unlockedWeaponsFlags: String = "1,1,0,0" // Match dynamic inventory lists
)
