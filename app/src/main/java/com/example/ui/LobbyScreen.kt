package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Color constants for Pokyon Theme
val PokyonDarkBg = Color(0xFF060912)
val PokyonSurface = Color(0xFF0E1322)
val PokyonCyan = Color(0xFF00FFCC)
val PokyonSpiritual = Color(0xFF00FF99)
val PokyonAlertPink = Color(0xFFFF007F)
val PokyonGreyText = Color(0xFF8C9BA5)

@Composable
fun LobbyScreen(
    state: LobbyUiState,
    viewModel: LobbyViewModel,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PokyonDarkBg),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(max = 720.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
        // App Header & Currency Bars
        item {
            LobbyHeader(state = state, viewModel = viewModel)
        }

        // Active selection summary (Map + Equipped stats)
        item {
            ActiveStatusSummary(state = state, viewModel = viewModel)
        }

        // Character Selector Row
        item {
            CharacterSelectorSection(state = state, viewModel = viewModel)
        }

        // Weapons Catalog Grid Scroll
        item {
            WeaponsCatalogSection(state = state, viewModel = viewModel)
        }

        // Tactical Maps Section Scroll List
        item {
            MapsCatalogSection(state = state, viewModel = viewModel)
        }

        // Console logger and system records
        item {
            ConsoleLoggerSection(state = state)
        }
    }
    }
}

@Composable
fun LobbyHeader(state: LobbyUiState, viewModel: LobbyViewModel) {
    Card(
        colors = CardDefaults.cardColors(containerColor = PokyonSurface),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, PokyonCyan.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "POKYON",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = PokyonCyan,
                        letterSpacing = 4.sp,
                    )
                    Text(
                        text = "Owner: ${state.ownerName}",
                        fontSize = 11.sp,
                        color = PokyonGreyText,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                // Currency capsules
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Mantra (premium)
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White.copy(alpha = 0.05f))
                            .border(1.dp, PokyonCyan, RoundedCornerShape(16.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Mantra Diamond",
                            tint = PokyonCyan,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${state.mantras} MANTRA",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = PokyonCyan
                        )
                    }

                    // Soul Coins (earned)
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White.copy(alpha = 0.05f))
                            .border(1.dp, PokyonSpiritual, RoundedCornerShape(16.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Soul Coins",
                            tint = PokyonSpiritual,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${state.soulCoins} SOULS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = PokyonSpiritual
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Time Limit Summary
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(PokyonDarkBg)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val iconColor = when (state.engineState) {
                        EngineState.ActiveGameplay -> PokyonSpiritual
                        else -> PokyonAlertPink
                    }
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Status",
                        tint = iconColor,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = when (state.engineState) {
                                EngineState.ActiveGameplay -> "SYSTEM OK: LOCOMOTION ENGAGED"
                                EngineState.EnforcedBreak -> "REST MANDATE: SCREEN BLACKOUT"
                                EngineState.DailySoftLock -> "DAILY LOCKOUT: 4 HOURS EXCEEDED"
                            },
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = when (state.engineState) {
                                EngineState.ActiveGameplay -> PokyonSpiritual
                                else -> PokyonAlertPink
                            }
                        )
                        Text(
                            text = "Daily Limit: 4.0 Hrs // Break: Every 30 Mins",
                            fontSize = 9.sp,
                            color = PokyonGreyText
                        )
                    }
                }

                // Clock timer
                val elapsedHr = state.totalPlaytimeSeconds / 3600
                val elapsedMin = (state.totalPlaytimeSeconds % 3600) / 60
                val elapsedSec = state.totalPlaytimeSeconds % 60
                Text(
                    text = String.format("CLOCK: %02d:%02d:%02d", elapsedHr, elapsedMin, elapsedSec),
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = PokyonCyan
                )
            }
        }
    }
}

@Composable
fun ActiveStatusSummary(state: LobbyUiState, viewModel: LobbyViewModel) {
    val activeAvatar = viewModel.avatars[state.selectedAvatarIndex]
    val activeMap = viewModel.maps[state.selectedMapIndex]
    val activeWeapon = viewModel.weapons[state.selectedWeaponIndex]

    Card(
        colors = CardDefaults.cardColors(containerColor = PokyonDarkBg),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(8.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = "DEPLOYMENT READOUT",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = PokyonGreyText,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("ACTIVE VANGUARD", fontSize = 9.sp, color = PokyonGreyText)
                    Text(activeAvatar.name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text("MAP COORDINATE", fontSize = 9.sp, color = PokyonGreyText)
                    Text(activeMap.name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = PokyonCyan)
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text("EQUIPPED WEAPON", fontSize = 9.sp, color = PokyonGreyText)
                    Text(activeWeapon.name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = PokyonSpiritual)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Pulse Enter Matrix button
            Button(
                onClick = { viewModel.setTab(1) }, // Jump to 3D View
                colors = ButtonDefaults.buttonColors(containerColor = PokyonCyan),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("submit_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Launch",
                    tint = PokyonDarkBg
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "ENTER 3D OPERATIONAL ARENA",
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp,
                    color = PokyonDarkBg,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Composable
fun CharacterSelectorSection(state: LobbyUiState, viewModel: LobbyViewModel) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "SELECT SQUAD VANGUARD (AAA PROMPTS)",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = PokyonCyan,
                letterSpacing = 2.sp
            )
            Text(
                text = "${state.selectedAvatarIndex + 1}/${viewModel.avatars.size}",
                fontSize = 11.sp,
                color = PokyonGreyText
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Horizontal scrolling avatars
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(viewModel.avatars) { idx, item ->
                val isSelected = state.selectedAvatarIndex == idx
                val brdColor = if (isSelected) PokyonCyan else Color.White.copy(alpha = 0.05f)
                val bgClr = if (isSelected) PokyonSurface else PokyonSurface.copy(alpha = 0.5f)

                Column(
                    modifier = Modifier
                        .width(130.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(bgClr)
                        .border(1.5.dp, brdColor, RoundedCornerShape(8.dp))
                        .clickable { viewModel.selectAvatar(idx) }
                        .padding(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(PokyonCyan.copy(alpha = 0.2f), PokyonDarkBg)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (idx) {
                                0 -> Icons.Default.Person
                                1 -> Icons.Default.Favorite
                                2 -> Icons.Default.Face
                                3 -> Icons.Default.Share
                                else -> Icons.Default.Star
                            },
                            contentDescription = item.name,
                            tint = if (isSelected) PokyonCyan else PokyonGreyText,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = item.name,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) PokyonCyan else Color.White
                    )
                    Text(
                        text = item.title,
                        fontSize = 9.sp,
                        color = PokyonGreyText,
                        maxLines = 1
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // selected character backstory panel
        val selectedChar = viewModel.avatars[state.selectedAvatarIndex]
        Card(
            colors = CardDefaults.cardColors(containerColor = PokyonSurface),
            modifier = Modifier
                .fillMaxWidth()
                .border(0.5.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(6.dp))
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = selectedChar.description,
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.85f),
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "MIDJOURNEY ART ASSET PROMPT:",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = PokyonCyan
                )
                Text(
                    text = selectedChar.prompt,
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    color = PokyonGreyText,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(PokyonDarkBg)
                        .padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun WeaponsCatalogSection(state: LobbyUiState, viewModel: LobbyViewModel) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "HUMAN WAR TIMELINE ARSENAL",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = PokyonCyan,
            letterSpacing = 2.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(viewModel.weapons) { idx, weapon ->
                val isSelected = state.selectedWeaponIndex == idx
                val bg = if (isSelected) PokyonSurface else PokyonSurface.copy(alpha = 0.5f)
                val stroke = if (isSelected) PokyonSpiritual else Color.White.copy(alpha = 0.05f)

                Column(
                    modifier = Modifier
                        .width(220.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(bg)
                        .border(1.5.dp, stroke, RoundedCornerShape(8.dp))
                        .clickable { viewModel.selectWeapon(idx) }
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = weapon.name,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Weapon",
                            tint = if (isSelected) PokyonSpiritual else PokyonGreyText,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(weapon.grade, fontSize = 9.sp, color = PokyonSpiritual, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(weapon.description, fontSize = 10.sp, color = PokyonGreyText, minLines = 2, maxLines = 2)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "ROF: ${weapon.rateOfFire}",
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        color = Color.White.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}

@Composable
fun MapsCatalogSection(state: LobbyUiState, viewModel: LobbyViewModel) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "TRADITIONAL CULTURE-BLENDED SITES",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = PokyonCyan,
            letterSpacing = 2.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(viewModel.maps) { idx, map ->
                val isSelected = state.selectedMapIndex == idx
                val bg = if (isSelected) PokyonSurface else PokyonSurface.copy(alpha = 0.5f)
                val stroke = if (isSelected) PokyonCyan else Color.White.copy(alpha = 0.05f)

                Column(
                    modifier = Modifier
                        .width(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(bg)
                        .border(1.5.dp, stroke, RoundedCornerShape(8.dp))
                        .clickable { viewModel.selectMap(idx) }
                        .padding(12.dp)
                ) {
                    Text(
                        text = map.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) PokyonCyan else Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = map.lore,
                        fontSize = 10.sp,
                        color = PokyonGreyText,
                        minLines = 3,
                        maxLines = 3
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "WEATHER: ${map.weather}",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = PokyonCyan.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}

@Composable
fun ConsoleLoggerSection(state: LobbyUiState) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "MESH TELEMETRY ENGINE CORES LOGS",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = PokyonGreyText,
            letterSpacing = 2.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .background(PokyonSurface)
                .border(0.5.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(6.dp))
                .padding(12.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                if (state.feedbackLog.isEmpty()) {
                    Text(
                        text = "> System handshake: initialized secure endpoints successfully.",
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        color = PokyonSpiritual
                    )
                } else {
                    state.feedbackLog.forEach { log ->
                        Text(
                            text = "> $log",
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            color = if (log.contains("Limit") || log.contains("LOCK")) PokyonAlertPink else PokyonSpiritual
                        )
                    }
                }
            }
        }
    }
}
