package com.example.ui

import android.annotation.SuppressLint
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import org.json.JSONObject

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ArenaScreen(
    state: LobbyUiState,
    viewModel: LobbyViewModel,
    modifier: Modifier = Modifier
) {
    var webViewInstance by remember { mutableStateOf<WebView?>(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PokyonDarkBg)
    ) {
        // Embed the operational WebGL 3D WebView with native integrations
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.useWideViewPort = true
                    settings.loadWithOverviewMode = true
                    settings.allowFileAccess = true
                    settings.allowContentAccess = true

                    webViewClient = object : WebViewClient() {
                        override fun onRenderProcessGone(
                            view: WebView?,
                            detail: android.webkit.RenderProcessGoneDetail?
                        ): Boolean {
                            // Recover gracefully if WebGL renderer crashes in restricted cloud containers
                            view?.loadUrl("about:blank")
                            return true
                        }
                    }

                    // Secure bridge link
                    addJavascriptInterface(object {
                        @JavascriptInterface
                        fun postMessage(message: String) {
                            try {
                                val json = JSONObject(message)
                                val event = json.optString("event")
                                val value = json.optString("value")

                                when (event) {
                                    "sanctuary" -> {
                                        val isInside = value == "inside"
                                        viewModel.updateSanctuaryStatus(isInside)
                                    }
                                    "combat" -> {
                                        if (value == "reload") {
                                            viewModel.addLog("Weapon Ammo loop reloaded natively.")
                                        }
                                    }
                                    "movement" -> {
                                        if (value == "jump") {
                                            viewModel.addLog("Mobile layout jump command acknowledged.")
                                        }
                                    }
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }, "AndroidBridge")

                    loadUrl("file:///android_asset/pokyon_game.html")
                    webViewInstance = this
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // WebGL game UI HUD in native compose coordinates
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Live Status Controls Top Overlays
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.statusBars),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Return to main lobby safely
                Button(
                    onClick = { viewModel.setTab(0) },
                    colors = ButtonDefaults.buttonColors(containerColor = PokyonSurface),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.border(1.dp, PokyonCyan.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                ) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = PokyonCyan)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("LOBBY", color = PokyonCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                // HP Indicator inside 3D
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(PokyonSurface)
                        .border(1.dp, PokyonSpiritual.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Health",
                        tint = PokyonSpiritual,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "HEALTH: ${state.playerHealth}%",
                        color = PokyonSpiritual,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Quick notice when inside Healing Sanctuary
            AnimatedVisibility(
                visible = state.insideSanctuary,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = PokyonSurface.copy(alpha = 0.9f)),
                    modifier = Modifier.border(1.dp, PokyonSpiritual, RoundedCornerShape(8.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            color = PokyonSpiritual,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "🛡️ HEALING SANCTUARY: +4% HEALTH AUTO REGEN",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = PokyonSpiritual
                        )
                    }
                }
            }

            // Help overlay floating above
            Card(
                colors = CardDefaults.cardColors(containerColor = PokyonSurface.copy(alpha = 0.85f)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 80.dp) // Cushion above joystick zone
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "CONTROLS: WASD to Move // Space to Jump // Shift to Sprint // C to Crouch // R to Reload",
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }

        // Apply visual updates to WebView when lock/unlock occurs
        LaunchedEffect(state.engineState) {
            val isLocked = state.engineState != EngineState.ActiveGameplay
            val msg = when (state.engineState) {
                EngineState.EnforcedBreak -> "30-MINUTES MANDATORY WELLNESS BREAK ACTIVED"
                EngineState.DailySoftLock -> "DAILY 4-HOURS PLAYTIME COMPLETED"
                else -> ""
            }
            webViewInstance?.evaluateJavascript(
                "if (typeof triggerExternalLockout === 'function') { triggerExternalLockout($isLocked, '$msg'); }", null
            )
        }

        // 3D Game screen block overlay (Mandatory Digital Cooldown System canvas)
        AnimatedVisibility(
            visible = state.engineState != EngineState.ActiveGameplay,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(PokyonDarkBg.copy(alpha = 0.98f))
                    .clickable { /* Block all user tap inputs completely */ },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .border(1.5.dp, PokyonAlertPink, RoundedCornerShape(12.dp))
                        .background(PokyonSurface)
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Lockout Shield",
                        tint = PokyonAlertPink,
                        modifier = Modifier.size(56.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "DIGITAL WELLNESS MANDATED REST CHANNELS",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = PokyonAlertPink,
                        letterSpacing = 2.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "In compliance with international digital health directives, your game session is locked.",
                        fontSize = 11.sp,
                        color = PokyonGreyText,
                        textAlign = TextAlign.Center,
                        lineHeight = 16.sp,
                        modifier = Modifier.fillMaxWidth(0.85f)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Simulated Countdown Clock
                    if (state.engineState == EngineState.EnforcedBreak) {
                        val breakMinutes = state.breakSecondsRemaining / 60
                        val breakSeconds = state.breakSecondsRemaining % 60
                        Text(
                            text = String.format("%02d:%02d", breakMinutes, breakSeconds),
                            fontSize = 44.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = PokyonAlertPink,
                            letterSpacing = 2.sp
                        )
                        Text(
                            text = "Cooldown break in progress. App will unlock automatically.",
                            fontSize = 10.sp,
                            color = PokyonGreyText,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    } else if (state.engineState == EngineState.DailySoftLock) {
                        Text(
                            text = "4-HOURS REST CYCLE ACTIVE",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = PokyonAlertPink,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "Daily playing hours exhausted. System resets at 00:00 midnight.",
                            fontSize = 10.sp,
                            color = PokyonGreyText,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Developer Speed Monitor tool to fast-track presentation testing
                    Text(
                        text = "PRESENTATION DEMO TOOL:",
                        fontSize = 9.sp,
                        color = PokyonGreyText.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = { viewModel.resetTimerDevOverride() },
                            colors = ButtonDefaults.buttonColors(containerColor = PokyonCyan),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text("DEV FORCE RESET COOLDOWN", color = PokyonDarkBg, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { viewModel.toggleSpeedMultiplier() },
                            colors = ButtonDefaults.buttonColors(containerColor = PokyonSurface),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.border(1.dp, PokyonCyan, RoundedCornerShape(6.dp))
                        ) {
                            Text(
                                text = "SPEED: ${state.speedMultiplier}x",
                                color = PokyonCyan,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
