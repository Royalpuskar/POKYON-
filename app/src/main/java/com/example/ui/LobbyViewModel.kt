package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.PlaytimeRecord
import com.example.data.PlaytimeRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class EngineState { ActiveGameplay, EnforcedBreak, DailySoftLock }

data class Avatar(
    val name: String,
    val title: String,
    val description: String,
    val prompt: String
)

data class GameMap(
    val name: String,
    val lore: String,
    val weather: String
)

data class Weapon(
    val name: String,
    val grade: String,
    val description: String,
    val rateOfFire: String
)

data class LobbyUiState(
    val ownerName: String = "ROYAL PUSKAR KHAS",
    val selectedAvatarIndex: Int = 0,
    val selectedMapIndex: Int = 0,
    val selectedWeaponIndex: Int = 0,
    val soulCoins: Int = 500,
    val mantras: Int = 50,
    val serverStatus: String = "GRID OPERATIONAL",
    val concurrentUsersStr: String = "4.82 Billion",
    val averagePingMs: Int = 18,
    val speedMultiplier: Int = 1, // 1 for real-time, 60 for speed presentation demo
    val totalPlaytimeSeconds: Long = 0L,
    val continuousSessionSeconds: Long = 0L,
    val breakSecondsRemaining: Long = 0L,
    val engineState: EngineState = EngineState.ActiveGameplay,
    val currentViewTab: Int = 0, // 0: Main Lobby, 1: 3D Arena, 2: DevOps, 3: Web Portal
    val showToastMessage: String? = null,
    val feedbackLog: List<String> = emptyList(),
    val insideSanctuary: Boolean = false,
    val playerHealth: Int = 100,
    val platformSelected: String = "ANDROID_APK", // "PC_WINDOWS", "ANDROID_APK", "IOS_XCODE"
    val isCompilingBuild: Boolean = false,
    val compileProgress: Float = 0f,
    val activeControlScheme: String = "MOBILE_TOUCH_CANVAS", // "PC_KEYBOARD_MOUSE", "MOBILE_TOUCH_CANVAS"
    val matchmakingActive: Boolean = false,
    val replicatedUsersCount: Int = 128
)

class LobbyViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PlaytimeRepository
    private val _uiState = MutableStateFlow(LobbyUiState())
    val uiState: StateFlow<LobbyUiState> = _uiState.asStateFlow()

    private var timeTrackerJob: Job? = null

    // Character profiles based on design prompts
    val avatars = listOf(
        Avatar(
            "Tenzin", 
            "The Himalayan Vanguard", 
            "Male Tibetan guardian mixing high-altitude tactical gear and spiritual geometric robes.", 
            "Cinematic 3D gaming character avatar showcase, male Tibetan warrior styling, wearing a futuristic technical tactical parka mixed with traditional geometric woven fabric patterns, background shows wooden mountain fortress temples..."
        ),
        Avatar(
            "Zoya", 
            "The Desert Tracker", 
            "Female Persian specialist equipped with tactical gold-trimmed cloaks and holographic bio-scanners.", 
            "Symmetrical 3D video game character portrait, female Persian specialist, tactical gold-trimmed desert cloak wrapped over lightweight body armor plates, emerald glowing monocle, backdrop of sandstone ruins..."
        ),
        Avatar(
            "Kaito", 
            "The Neon Shinobi", 
            "A modern stealth operator from Tokyo adorned with a wooden kitsune war-mask and glowing optics.", 
            "Male cyberpunk ninja character avatar icon, traditional Japanese wooden mask worn on side, colorful high-tech fiber-optic modern streetwear, cherry blossom petals drifting, game select preview style..."
        ),
        Avatar(
            "Freyja", 
            "The Frost Warden", 
            "A heavy armor Scandinavian commando customized with warm traditional furs and runic war paint.", 
            "Female Nordic combat operator game avatar, weathered silver hair, wearing heavy fur tactical neck guards over industrial metallic body chest armor, runic glowing cyan war paint, background of frozen shipyards..."
        ),
        Avatar(
            "Arjun", 
            "The Astra Archer", 
            "spiritual Archer of cosmic energy, utilizing ancient golden crowns and glowing arm-gauntlet launchers.", 
            "Male Indian futuristic spiritual combat warrior, crown-comms integrated headband, holding an unlit neon energy arm-gauntlet weapon bow, deep navy utility vest, golden hour spiritual mist background..."
        )
    )

    // Legends maps
    val maps = listOf(
        GameMap("Sky Temple Valley", "Himalayan pagodas woven over Andean mountain-terraces. Extreme heights.", "Misty Winds"),
        GameMap("Crimson Dune Kingdom", "Vast Saharan desert hiding majestic Persian oases and subterranean sanctuaries.", "Sandstorm Alert"),
        GameMap("Sakura Neon District", "Traditional East Asian quiet temples surrounded by ultra-dense glass cyberpunk towers.", "Neon Rainfall"),
        GameMap("Nordic Frost Harbor", "Freezing Scandinavian fjords packed with Celtic status monoliths and ice drifts.", "Blizzard Peak"),
        GameMap("Emerald Rain Basin", "Amazonian river gorges hiding ancient South Asian stepwells and step-terraces.", "Monsoon Rain")
    )

    // Mahabharata to Modern Weapons lists
    val weapons = listOf(
        Weapon("Astra Force Launcher", "Mythological Grade", "Forearm gauntlet firing sound-wave tracking tracking energy projectiles.", "0.15s per burst"),
        Weapon("Gada Kinetic Shockwaves", "Mythological Grade_Gada", "Heavy stone mace giving 360-degree ground-shattering radial rings.", "Melee Sweep"),
        Weapon("Ballistic EMP grenades", "Modern Tactical", "EMP grenades disabling electronics, electronic scopes, and vehicles instantly.", "Launcher lob"),
        Weapon("Radiation Deterrents", "Nuclear Zoning", "Zoning grenade creating radioactive localized clouds for battlefield management.", "Zoning cloud")
    )

    init {
        val database = AppDatabase.getDatabase(application)
        repository = PlaytimeRepository(database.playtimeDao())
        
        // Fetch saved records from Room Database
        viewModelScope.launch {
            repository.playtimeRecord.collect { record ->
                if (record != null) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            totalPlaytimeSeconds = if (currentState.totalPlaytimeSeconds == 0L) record.totalPlaytimeSeconds else currentState.totalPlaytimeSeconds,
                            continuousSessionSeconds = if (currentState.continuousSessionSeconds == 0L) record.continuousSessionSeconds else currentState.continuousSessionSeconds,
                            soulCoins = record.soulCoins,
                            mantras = record.mantras,
                            selectedAvatarIndex = record.activeAvatarId,
                            selectedMapIndex = record.activeMapId
                        )
                    }
                    val currentTracked = _uiState.value
                    validateCurrentTimeLimits(currentTracked.totalPlaytimeSeconds, currentTracked.continuousSessionSeconds)
                } else {
                    // Initialize clean database state for Royal Puskar Khas
                    val defaultRecord = PlaytimeRecord(id = 1)
                    repository.saveRecord(defaultRecord)
                }
            }
        }

        // Start Clock Engine
        startTimeTrackingLoop()
    }

    private fun validateCurrentTimeLimits(totalSeconds: Long, continuousSeconds: Long) {
        val limit4Hours = 14400L // 4 Hours in Seconds
        val session30Mins = 1800L // 30 mins in Seconds

        _uiState.update { state ->
            when {
                totalSeconds >= limit4Hours -> {
                    state.copy(
                        engineState = EngineState.DailySoftLock,
                        breakSecondsRemaining = 0
                    )
                }
                continuousSeconds >= session30Mins -> {
                    val remainingBreak = session30Mins - state.breakSecondsRemaining
                    state.copy(
                        engineState = EngineState.EnforcedBreak,
                        // If breakSecondsRemaining is not set yet, initialize to 30 minutes (1800s)
                        breakSecondsRemaining = if (state.breakSecondsRemaining <= 0) 1800 else state.breakSecondsRemaining
                    )
                }
                else -> {
                    state.copy(
                        engineState = EngineState.ActiveGameplay,
                        breakSecondsRemaining = 0
                    )
                }
            }
        }
    }

    private fun startTimeTrackingLoop() {
        timeTrackerJob?.cancel()
        timeTrackerJob = viewModelScope.launch {
            while (true) {
                delay(1000L) // Tick every 1 second
                val state = _uiState.value
                val step = state.speedMultiplier // Ticks faster if speedMultiplier is set to 60 for demoing!

                when (state.engineState) {
                    EngineState.ActiveGameplay -> {
                        // Increment time metrics
                        val newTotal = state.totalPlaytimeSeconds + step
                        val newSession = state.continuousSessionSeconds + step

                        _uiState.update { 
                            it.copy(
                                totalPlaytimeSeconds = newTotal,
                                continuousSessionSeconds = newSession
                            )
                        }

                        // Save update periodically into local cache (optimized to save every 10 seconds)
                        if (newTotal % 10 == 0L) {
                            viewModelScope.launch {
                                repository.updateTimers(newTotal, newSession)
                            }
                        }

                        // Check triggers
                        if (newTotal >= 14400L) { // 4 Hours daily limit reached
                            _uiState.update { 
                                it.copy(
                                    engineState = EngineState.DailySoftLock,
                                    currentViewTab = 0 // Kick back to main lobby display
                                ) 
                            }
                            addLog("Anti-Addiction system: 4-Hour Daily Play Limit reached. System LOCK active!")
                        } else if (newSession >= 1800L) { // 30-min session reached
                            _uiState.update { 
                                it.copy(
                                    engineState = EngineState.EnforcedBreak,
                                    breakSecondsRemaining = 1800L // 30 minutes break tracker starts
                                ) 
                            }
                            addLog("Anti-Addiction system: Continuous Play limit (30 Mins) crossed. Initiating 30-Min Enforced Break!")
                        }

                        // Health regeneration while inside healing sanctuary (4% per second)
                        if (state.insideSanctuary) {
                            if (state.playerHealth < 100) {
                                val gainedHealth = (state.playerHealth + 4).coerceAtMost(100)
                                _uiState.update { it.copy(playerHealth = gainedHealth) }
                                if (gainedHealth >= 100) {
                                    addLog("Sacred Healing complete. Aura life stabilized at 100%.")
                                }
                            }
                        }
                    }

                    EngineState.EnforcedBreak -> {
                        val remainingBreakVal = state.breakSecondsRemaining - step
                        if (remainingBreakVal <= 0) {
                            // Break successfully completed, reset countdowns
                            _uiState.update { 
                                it.copy(
                                    engineState = EngineState.ActiveGameplay,
                                    continuousSessionSeconds = 0L,
                                    breakSecondsRemaining = 0L
                                ) 
                            }
                            viewModelScope.launch {
                                repository.updateTimers(state.totalPlaytimeSeconds, 0L)
                            }
                            addLog("Anti-Addiction break complete. Locomotion arrays re-enabled. Authenticated!")
                        } else {
                            _uiState.update { it.copy(breakSecondsRemaining = remainingBreakVal) }
                        }
                    }

                    EngineState.DailySoftLock -> {
                        // Locked until daily reset (Can be unlocked inside dev dashboard for easy testing)
                    }
                }

                // Randomly drift concurrent user metrics slightly to show real server orchestration dynamics
                _uiState.update { currentState ->
                    val pingDrift = (-2..2).random()
                    val newPing = (currentState.averagePingMs + pingDrift).coerceIn(12, 35)
                    val userCountDrift = (1..5).random() / 100.0
                    val isUp = (0..1).random() == 1
                    val currentDouble = currentState.concurrentUsersStr.split(" ")[0].toDoubleOrNull() ?: 4.85
                    val updatedDouble = if (isUp) currentDouble + userCountDrift else currentDouble - userCountDrift
                    currentState.copy(
                        averagePingMs = newPing,
                        concurrentUsersStr = String.format("%.2f Billion", updatedDouble)
                    )
                }
            }
        }
    }

    fun selectAvatar(index: Int) {
        if (index in avatars.indices) {
            _uiState.update { it.copy(selectedAvatarIndex = index) }
            addLog("Select Vanguard profile: ${avatars[index].name}")
            saveLobbyState()
        }
    }

    fun selectMap(index: Int) {
        if (index in maps.indices) {
            _uiState.update { it.copy(selectedMapIndex = index) }
            addLog("Map Sector coordinates updated to: ${maps[index].name}")
            saveLobbyState()
        }
    }

    fun selectWeapon(index: Int) {
        if (index in weapons.indices) {
            _uiState.update { it.copy(selectedWeaponIndex = index) }
            addLog("Equipped primary gun: ${weapons[index].name}")
        }
    }

    fun toggleSpeedMultiplier() {
        _uiState.update {
            val nextMult = if (it.speedMultiplier == 1) 60 else 1
            it.copy(
                speedMultiplier = nextMult,
                showToastMessage = if (nextMult == 60) "Presentation Speed mode enabled (60x time loop)" else "Real-time clock monitoring enabled (1s)"
            )
        }
    }

    fun setTab(tabId: Int) {
        _uiState.update { it.copy(currentViewTab = tabId) }
    }

    fun updateSanctuaryStatus(isInside: Boolean) {
        _uiState.update { it.copy(insideSanctuary = isInside) }
        if (isInside) {
            addLog("Entering Sanctuary. Weapon arrays offline. Healing matrix active (+4% HP/sec).")
            // Award Soul Coins for finding peace
            viewModelScope.launch {
                repository.addSoulCoins(10)
            }
        } else {
            addLog("Exiting Sanctuary. Reloading weapon ammo loops.")
        }
    }

    fun resetTimerDevOverride() {
        _uiState.update {
            it.copy(
                totalPlaytimeSeconds = 0L,
                continuousSessionSeconds = 0L,
                breakSecondsRemaining = 0L,
                engineState = EngineState.ActiveGameplay,
                showToastMessage = "Developer Reset: Timers set to zero."
            )
        }
        viewModelScope.launch {
            repository.updateTimers(0L, 0L)
        }
        addLog("Dev Override: System playtime and soft-locks manually reset.")
    }

    fun simulateTopupWithSplit(amount: Int) {
        val txId = "TX-NPL-" + (1000..9999).random() + "-" + (1000..9999).random()
        val baseExclusive = (amount / 1.13)
        val vatAmount = amount - baseExclusive
        val primaryOps = baseExclusive * 0.70
        val secondaryReserve = baseExclusive * 0.30

        // Grant core premium Mantra currency
        _uiState.update { currentState ->
            val updatedMantras = currentState.mantras + amount
            currentState.copy(
                mantras = updatedMantras,
                showToastMessage = "Sovereign top-up complete! +$amount Mantra Coins added."
            )
        }
        
        // Save state to SQLite Room repository
        saveLobbyState()

        // Push detailed technical settlement logs to the central console logger
        addLog("NPR Top-Up initiated: NPR $amount via eSewa v2. TxUUID: $txId")
        addLog("Crypto Signature: HMAC_SHA256_VERIFIED_SUCCESFULLY_ROYAL")
        addLog("Nepal IRD VAT Audit: Gross $amount | Base ${String.format("%.2f", baseExclusive)} | 13% VAT ${String.format("%.2f", vatAmount)}")
        addLog("Split Route A [70% Ops]: Routed Rs. ${String.format("%.2f", primaryOps)} to NABIL BANK (No: 0110017502781)")
        addLog("Split Route B [30% Reserve]: Routed Rs. ${String.format("%.2f", secondaryReserve)} to NIC ASIA BANK (No: 1202874910274)")
        addLog("Redis Sync: Immutable ledger committed to cluster successfully.")
    }

    fun selectPlatformTarget(platform: String) {
        _uiState.update { it.copy(platformSelected = platform) }
        val platformLabel = when(platform) {
            "PC_WINDOWS" -> "PC, Mac & Linux Standalone"
            "ANDROID_APK" -> "Android Mobile Platform (APK)"
            "IOS_XCODE" -> "iOS Apple Platform (Xcode)"
            else -> platform
        }
        addLog("Target Build Configured: $platformLabel Selected.")
    }

    fun triggerCrossPlatformCompile() {
        if (_uiState.value.isCompilingBuild) return
        _uiState.update { it.copy(isCompilingBuild = true, compileProgress = 0f) }
        addLog("Starting Unity Cross-Platform compilation compiler chain...")
        
        viewModelScope.launch {
            val steps = 5
            for (i in 1..steps) {
                delay(600)
                val progress = (i.toFloat() / steps.toFloat())
                _uiState.update { it.copy(compileProgress = progress) }
                when (i) {
                    1 -> addLog("Resolving dependency packages... CrossPlatformInput system configured.")
                    2 -> addLog("Parsing PokyonCrossPlatformEngine input bindings...")
                    3 -> if (_uiState.value.platformSelected == "PC_WINDOWS") {
                        addLog("Generating PC Direct3D/Vulkan Shader variants...")
                    } else if (_uiState.value.platformSelected == "ANDROID_APK") {
                        addLog("Stripping unused Android assets & compiling APK executable...")
                    } else {
                        addLog("Generating Apple Objective-C / Swift project directory structure...")
                    }
                    4 -> addLog("Integrating matchmaking socket replication handshake hooks...")
                    5 -> {
                        val buildArtifact = when(_uiState.value.platformSelected) {
                            "PC_WINDOWS" -> "PokyonGame.exe (Windows)"
                            "ANDROID_APK" -> "PokyonGame.apk (Android)"
                            "IOS_XCODE" -> "Xcode Project Folder (iOS)"
                            else -> "Bundle"
                        }
                        _uiState.update { 
                            it.copy(
                                isCompilingBuild = false,
                                showToastMessage = "Cross-Compilation complete! $buildArtifact generated."
                            ) 
                        }
                        addLog("COMPILATION SUCCESS: Generated artifact: $buildArtifact.")
                    }
                }
            }
        }
    }

    fun toggleControlScheme() {
        _uiState.update { currentState ->
            val nextScheme = if (currentState.activeControlScheme == "MOBILE_TOUCH_CANVAS") {
                "PC_KEYBOARD_MOUSE"
            } else {
                "MOBILE_TOUCH_CANVAS"
            }
            currentState.copy(
                activeControlScheme = nextScheme,
                showToastMessage = "Input scheme toggled to ${if (nextScheme == "PC_KEYBOARD_MOUSE") "WASD / Mouse Aim" else "Mobile Touch Layout"}"
            )
        }
        // Force evaluation sync of logging
        val actualScheme = _uiState.value.activeControlScheme
        addLog("Control mode updated: Active input reads optimized for " + 
            if (actualScheme == "PC_KEYBOARD_MOUSE") "Laptop WASD + Mouse Pointing" else "Mobile On-screen Joysticks")
    }

    fun toggleMatchmaking() {
        val curr = _uiState.value.matchmakingActive
        _uiState.update { it.copy(matchmakingActive = !curr) }
        val nextVal = _uiState.value.matchmakingActive
        if (nextVal) {
            addLog("Socket Room Matchmaker online. Registering cross-play replication lobby.")
            addLog("Active socket session: Listening on UDP Port 7777.")
            // Simulate joining players
            viewModelScope.launch {
                delay(800)
                if (_uiState.value.matchmakingActive) {
                    _uiState.update { it.copy(replicatedUsersCount = 134) }
                    addLog("[Room Matcher] Connected: PC player 'Vanguard_Khas_PC' joined.")
                }
                delay(800)
                if (_uiState.value.matchmakingActive) {
                    _uiState.update { it.copy(replicatedUsersCount = 139) }
                    addLog("[Room Matcher] Connected: iOS player 'Zoya_Sovereign_iOS' entered.")
                }
            }
        } else {
            _uiState.update { it.copy(replicatedUsersCount = 120) }
            addLog("Socket Room Matchmaker offline. Reverted to local practice sandbox.")
        }
    }

    fun clearToast() {
        _uiState.update { it.copy(showToastMessage = null) }
    }

    fun addLog(msg: String) {
        _uiState.update {
            val updated = it.feedbackLog.takeLast(14) + msg
            it.copy(feedbackLog = updated)
        }
    }

    private fun saveLobbyState() {
        viewModelScope.launch {
            val current = _uiState.value
            repository.saveRecord(
                PlaytimeRecord(
                    id = 1,
                    totalPlaytimeSeconds = current.totalPlaytimeSeconds,
                    continuousSessionSeconds = current.continuousSessionSeconds,
                    soulCoins = current.soulCoins,
                    mantras = current.mantras,
                    activeAvatarId = current.selectedAvatarIndex,
                    activeMapId = current.selectedMapIndex
                )
            )
        }
    }

    override fun onCleared() {
        timeTrackerJob?.cancel()
        // Save the final tracked timers upon ViewModel teardown
        val current = _uiState.value
        viewModelScope.launch {
            repository.updateTimers(current.totalPlaytimeSeconds, current.continuousSessionSeconds)
        }
        super.onCleared()
    }
}
