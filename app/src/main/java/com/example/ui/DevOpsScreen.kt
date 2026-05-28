package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DevOpsScreen(
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
        // Architecture Overview
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = PokyonSurface),
                modifier = Modifier.border(1.dp, PokyonCyan.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "5-BILLION PLAYER ARCHITECTURE",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = PokyonCyan,
                            letterSpacing = 2.sp
                        )
                        Icon(imageVector = Icons.Default.Build, contentDescription = "Orchestrations", tint = PokyonCyan)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "To handle massive global workloads continuously, user session timers are validated strictly on Redis cache groups via distributed Anycast routing grids. Device locks are immutable and server-authoritative.",
                        fontSize = 11.sp,
                        color = PokyonGreyText,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Live Telemetry Grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Stat 1: Replicas
                Card(
                    colors = CardDefaults.cardColors(containerColor = PokyonSurface),
                    modifier = Modifier
                        .weight(1f)
                        .border(0.5.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(8.dp))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("ACTIVE PODS", fontSize = 9.sp, color = PokyonGreyText)
                        Text("43,182", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = PokyonSpiritual)
                        Text("/ 50,000 Target Node Clusters", fontSize = 8.sp, color = PokyonGreyText)
                    }
                }

                // Stat 2: User count
                Card(
                    colors = CardDefaults.cardColors(containerColor = PokyonSurface),
                    modifier = Modifier
                        .weight(1f)
                        .border(0.5.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(8.dp))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("CONCURRENT USERS", fontSize = 9.sp, color = PokyonGreyText)
                        Text(state.concurrentUsersStr, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = PokyonCyan)
                        Text("Distributed globally", fontSize = 8.sp, color = PokyonGreyText)
                    }
                }
            }
        }

        // Latency details
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = PokyonSurface),
                modifier = Modifier.border(0.5.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(8.dp))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("EDGE DNS INTERACTIVE TIMEOUT", fontSize = 10.sp, color = PokyonGreyText)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("AVERAGE PING: ${state.averagePingMs}ms", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("HEALTH: 100% OPERATIONAL", fontSize = 10.sp, color = PokyonSpiritual, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Developer tools override
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = PokyonSurface),
                modifier = Modifier.border(1.dp, PokyonAlertPink.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "DEVOPS TIME CONTROLS (TEST RIGS)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = PokyonAlertPink,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Manually speed up progress to bypass real-time wait limits. Demonstration modes speeds timing calculation loop by sixty-fold.",
                        fontSize = 10.sp,
                        color = PokyonGreyText
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { viewModel.toggleSpeedMultiplier() },
                            colors = ButtonDefaults.buttonColors(containerColor = PokyonAlertPink),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(imageVector = Icons.Default.Refresh, contentDescription = "Speed", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "TOGGLE SPEED (${state.speedMultiplier}X)", 
                                fontSize = 11.sp, 
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = { viewModel.resetTimerDevOverride() },
                            colors = ButtonDefaults.buttonColors(containerColor = PokyonDarkBg),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, PokyonAlertPink, RoundedCornerShape(6.dp))
                        ) {
                            Text("FORCE TIME RESET", color = PokyonAlertPink, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Virtual Unity Engine Scene Inspector Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = PokyonSurface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, PokyonCyan.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🎮 VIRTUAL UNITY SCENE INSPECTOR",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = PokyonCyan,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Verify the physical maps structural components from your setup. These items are compiled seamlessly and live-simulating inside the WebGL Engine.",
                        fontSize = 10.sp,
                        color = PokyonGreyText
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Ground Terrain Component Description
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(PokyonDarkBg)
                            .padding(8.dp)
                    ) {
                        Text("1. PHYSICAL 3D GROUND (Terrain / Floor Plane)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = PokyonCyan)
                        Text("• Geometry: Plane Mesh (200 x 200 units)\n• Material: Matte Slate Standard\n• Safe-Locks: Gravity drops simulated perfectly, preventing fall voids.", fontSize = 9.sp, color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Player Character Capsule Component Description
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(PokyonDarkBg)
                            .padding(8.dp)
                    ) {
                        Text("2. PLAYER CHARACTER (Capsule Controller Object)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = PokyonCyan)
                        Text("• Object: Capsule Group Primitive (Body + Helm + Gauntlet)\n• Behavior: Character Controller class tracking walk/sprint/stance shifts.", fontSize = 9.sp, color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Camera & HUD Connections
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(PokyonDarkBg)
                            .padding(8.dp)
                    ) {
                        Text("3. CAMERA & PUBLIC HUD LINKS (Main Camera Bindings)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = PokyonCyan)
                        Text("• Over-Shoulder (OTS): Standard Camera tracking target lerps.\n• public Text/Bar variables: Canvas health and ammo meters are fully tracked.\n• State Synced: Plays dynamic recoil shake on launcher discharge.", fontSize = 9.sp, color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Safe Sanctuary Trigger
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(PokyonDarkBg)
                            .padding(8.dp)
                    ) {
                        Text("4. HEALING SANCTUARY TRIGGER (Cube / Cylinder Volume)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = PokyonCyan)
                        Text("• Transform Location: Cylinder center (0, 7.5, -25)\n• Trigger Tag: \"HealingSanctuary\" | Is Trigger: TRUE\n• Physics: Radius detection < 6.0 units triggers glowing aura and live heal factor.", fontSize = 9.sp, color = Color.White)
                    }
                }
            }
        }

        // Nepalese Payment & Ledger Split Route Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = PokyonSurface),
                modifier = Modifier.border(1.dp, PokyonSpiritual.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🇳🇵 NEPALESE SECURE MULTI-BANK ROUTING",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = PokyonSpiritual,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Real-time 70% Nabil Bank / 30% NIC Asia Bank programmatic splits with instant HMAC-SHA256 signature verification and Redis ledger logging.",
                        fontSize = 10.sp,
                        color = PokyonGreyText
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Display target bank accounts
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(4.dp))
                            .background(PokyonDarkBg)
                            .padding(8.dp)
                    ) {
                        Text("TARGET NEPAL SETTLEMENT CHANNELS:", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = PokyonSpiritual)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("• Account A (70% Operations): NABIL BANK LTD\n  No: 0110017502781 | Name: POKYON ENT OPS", fontSize = 9.sp, color = Color.White)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("• Account B (30% Reserve): NIC ASIA BANK LTD\n  No: 1202874910274 | Name: POKYON ENT SEC CAP", fontSize = 9.sp, color = Color.White)
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { viewModel.simulateTopupWithSplit(1500) },
                        colors = ButtonDefaults.buttonColors(containerColor = PokyonSpiritual),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("SIMULATE NPR 1500 eSewa/Khalti SPLIT TOP-UP", color = PokyonDarkBg, fontSize = 11.sp, fontWeight = FontWeight.Black)
                    }
                }
            }
        }

        // Unity Cross-Platform Compiler & Build Channels Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = PokyonSurface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, PokyonCyan.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🚀 CROSS-PLAY COMPILER & TARGET BUILD CHANNELS",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = PokyonCyan,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Build standalone game binaries across PC/Mac, Android, and iOS using optimized multi-target compiler pipelines.",
                        fontSize = 10.sp,
                        color = PokyonGreyText
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Text("SELECT PLATFORM BUILD TARGET:", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = PokyonGreyText)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            "PC_WINDOWS" to "💻 PC/Mac",
                            "ANDROID_APK" to "📱 Android",
                            "IOS_XCODE" to "🍎 iOS Xcode"
                        ).forEach { (code, label) ->
                            val isSelected = state.platformSelected == code
                            Button(
                                onClick = { viewModel.selectPlatformTarget(code) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSelected) PokyonCyan else PokyonDarkBg
                                ),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .border(
                                        1.dp,
                                        if (isSelected) PokyonCyan else Color.White.copy(alpha = 0.1f),
                                        RoundedCornerShape(6.dp)
                                    ),
                                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    label,
                                    color = if (isSelected) PokyonDarkBg else Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Black else FontWeight.Normal
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Compilation progress view
                    if (state.isCompilingBuild || state.compileProgress > 0f) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(PokyonDarkBg)
                                .padding(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = if (state.isCompilingBuild) "⚡ COMPILING ASSETS..." else "✅ COMPILATION COMPLETE",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (state.isCompilingBuild) PokyonCyan else PokyonSpiritual
                                )
                                Text("${(state.compileProgress * 100).toInt()}%", fontSize = 9.sp, color = Color.White)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = state.compileProgress,
                                modifier = Modifier.fillMaxWidth().height(4.dp),
                                color = if (state.isCompilingBuild) PokyonCyan else PokyonSpiritual,
                                trackColor = Color.White.copy(alpha = 0.1f)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    Button(
                        enabled = !state.isCompilingBuild,
                        onClick = { viewModel.triggerCrossPlatformCompile() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PokyonCyan,
                            disabledContainerColor = PokyonDarkBg
                        ),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                1.dp,
                                if (state.isCompilingBuild) PokyonCyan.copy(alpha = 0.2f) else PokyonCyan,
                                RoundedCornerShape(6.dp)
                            )
                    ) {
                        Text(
                            if (state.isCompilingBuild) "COMPILER CHAIN ACTIVE..." else "RUN PROGRAMMATIC CROSS-COMPILE CHAIN",
                            color = PokyonDarkBg,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }
        }

        // Multi-Platform Input System Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = PokyonSurface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, PokyonSpiritual.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🎮 UNITY MULTI-PLATFORM INPUT CONTROLLER",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = PokyonSpiritual,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Define and test input system handlers responsive to keyboard/mouse mappings and on-screen multi-touch overlays.",
                        fontSize = 10.sp,
                        color = PokyonGreyText
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(PokyonDarkBg)
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("ACTIVE RECEPTOR MAPPINGS:", fontSize = 8.sp, color = PokyonGreyText)
                            Text(
                                text = if (state.activeControlScheme == "PC_KEYBOARD_MOUSE") "💻 LAPTOP WORKSPACE (WASD + ACTION)" else "📱 MOBILE HUD (ON-SCREEN JOYS)",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = PokyonSpiritual
                            )
                        }
                        Button(
                            onClick = { viewModel.toggleControlScheme() },
                            colors = ButtonDefaults.buttonColors(containerColor = PokyonSpiritual),
                            shape = RoundedCornerShape(4.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.height(28.dp)
                        ) {
                            Text("SWAP", color = PokyonDarkBg, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Display Input mappings blueprint
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(PokyonDarkBg)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = if (state.activeControlScheme == "PC_KEYBOARD_MOUSE") {
                                "• Movement: WASD / Arrow Keys (Raw input axis)\n• Sprint Action: hold LeftShift speed vector 11.0f\n• Gun discharging: Left-Click / Space Jump trigger\n• Mouse Cursor: Locked inside viewport (#if Standalone)"
                            } else {
                                "• Movement: On-screen Simulated Joysticks (< 45px drag radius)\n• Sprint Action: Holding 'SPRINT' touch HUD button scale\n• Gun discharging: Tapping bottom action-row 'FIRE' trigger\n• Jump Action: Tapping on-screen 'JMP' circular widget"
                            },
                            fontSize = 9.sp,
                            color = Color.White,
                            fontFamily = FontFamily.Monospace,
                            lineHeight = 13.sp
                        )
                    }
                }
            }
        }

        // Multiplayer Matching & Connection Mesh Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = PokyonSurface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, PokyonCyan.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🌐 MULTIPLAYER NETWORK ROOM REPLICATION",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = PokyonCyan,
                            letterSpacing = 1.sp
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (state.matchmakingActive) PokyonSpiritual.copy(alpha = 0.15f) else PokyonAlertPink.copy(alpha = 0.15f))
                                .border(0.5.dp, if (state.matchmakingActive) PokyonSpiritual else PokyonAlertPink, RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                if (state.matchmakingActive) "ROOM ACTIVE" else "SANDBOX MODE",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (state.matchmakingActive) PokyonSpiritual else PokyonAlertPink
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Establish a high-volume UDP socket room channel, enabling PC standalone players and Mobile touch package client clusters to connect synchronously.",
                        fontSize = 10.sp,
                        color = PokyonGreyText
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(PokyonDarkBg)
                                .padding(8.dp)
                        ) {
                            Text("MULTIPLAYER PROTOCOLS:", fontSize = 8.sp, color = PokyonGreyText)
                            Text("• Port: UDP 7777 Socket\n• Sync Protocol: RPC\n• Rate frequency: 60Hz", fontSize = 9.sp, color = Color.White)
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(PokyonDarkBg)
                                .padding(8.dp)
                        ) {
                            Text("CONNECTED CLIENT MESH:", fontSize = 8.sp, color = PokyonGreyText)
                            Text("• Active Lobby: ${state.replicatedUsersCount} users\n• Replication: UDP Socket\n• Cross-Play Status: Standby", fontSize = 9.sp, color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { viewModel.toggleMatchmaking() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (state.matchmakingActive) PokyonAlertPink else PokyonCyan
                        ),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (state.matchmakingActive) "STOP ROOM MATCHMAKER GATEWAY" else "INITIALIZE MATCHMAKING SOCKET GATEWAY",
                            color = if (state.matchmakingActive) Color.White else PokyonDarkBg,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }
        }

        // Kubernetes Manifest Blueprint
        item {
            Column {
                Text(
                    text = "DEPLOYMENT-MESH.YAML BLUEPRINT",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = PokyonCyan,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(PokyonSurface)
                        .padding(16.dp)
                ) {
                    Text(
                        text = """
                            apiVersion: apps/v1
                            kind: Deployment
                            metadata:
                              name: pokyon-global-core-mesh
                              labels:
                                app: pokyon-multiplayer
                            spec:
                              replicas: 50000
                              selector:
                                matchLabels:
                                  tier: game-server-tick
                              template:
                                metadata:
                                  labels:
                                    tier: game-server-tick
                                spec:
                                  containers:
                                  - name: pokyon-tick-processor
                                    image: pokyon-registry.gcr.io/core:v1.0.0
                                    resources:
                                      requests:
                                        memory: "4Gi"
                                        cpu: "2"
                                      limits:
                                        memory: "8Gi"
                                        cpu: "4"
                                    ports:
                                    - containerPort: 7777
                                      protocol: UDP
                        """.trimIndent(),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        color = PokyonGreyText,
                        lineHeight = 14.sp
                    )
                }
            }
        }
    }
    }
}
