package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.LobbyViewModel
import com.example.ui.LobbyScreen
import com.example.ui.ArenaScreen
import com.example.ui.DevOpsScreen
import com.example.ui.WebPortalScreen
import com.example.ui.PokyonDarkBg
import com.example.ui.PokyonCyan
import com.example.ui.PokyonSurface

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme(darkTheme = true, dynamicColor = false) {
                val context = LocalContext.current
                val lobbyViewModel: LobbyViewModel = viewModel()
                val uiState by lobbyViewModel.uiState.collectAsState()

                // Trigger toast alerts dynamically
                LaunchedEffect(uiState.showToastMessage) {
                    uiState.showToastMessage?.let { msg ->
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        lobbyViewModel.clearToast()
                    }
                }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(PokyonDarkBg),
                    bottomBar = {
                        NavigationBar(
                            containerColor = PokyonSurface,
                            modifier = Modifier.navigationBarsPadding(),
                            tonalElevation = NavigationBarDefaults.Elevation
                        ) {
                            NavigationBarItem(
                                selected = uiState.currentViewTab == 0,
                                onClick = { lobbyViewModel.setTab(0) },
                                label = { Text("LOBBY", fontSize = 10.sp) },
                                icon = { Icon(Icons.Default.Home, contentDescription = "Lobby") },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = PokyonCyan,
                                    selectedTextColor = PokyonCyan,
                                    indicatorColor = PokyonSurface,
                                    unselectedIconColor = Color.White.copy(alpha = 0.4f),
                                    unselectedTextColor = Color.White.copy(alpha = 0.4f)
                                )
                            )

                            NavigationBarItem(
                                selected = uiState.currentViewTab == 1,
                                onClick = { lobbyViewModel.setTab(1) },
                                label = { Text("3D ARENA", fontSize = 10.sp) },
                                icon = { Icon(Icons.Default.PlayArrow, contentDescription = "3D Simulation") },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = PokyonCyan,
                                    selectedTextColor = PokyonCyan,
                                    indicatorColor = PokyonSurface,
                                    unselectedIconColor = Color.White.copy(alpha = 0.4f),
                                    unselectedTextColor = Color.White.copy(alpha = 0.4f)
                                )
                            )

                            NavigationBarItem(
                                selected = uiState.currentViewTab == 2,
                                onClick = { lobbyViewModel.setTab(2) },
                                label = { Text("DEVOPS", fontSize = 10.sp) },
                                icon = { Icon(Icons.Default.Build, contentDescription = "DevOps") },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = PokyonCyan,
                                    selectedTextColor = PokyonCyan,
                                    indicatorColor = PokyonSurface,
                                    unselectedIconColor = Color.White.copy(alpha = 0.4f),
                                    unselectedTextColor = Color.White.copy(alpha = 0.4f)
                                )
                            )

                            NavigationBarItem(
                                selected = uiState.currentViewTab == 3,
                                onClick = { lobbyViewModel.setTab(3) },
                                label = { Text("WEB PORTAL", fontSize = 10.sp) },
                                icon = { Icon(Icons.Default.Info, contentDescription = "Web Hub") },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = PokyonCyan,
                                    selectedTextColor = PokyonCyan,
                                    indicatorColor = PokyonSurface,
                                    unselectedIconColor = Color.White.copy(alpha = 0.4f),
                                    unselectedTextColor = Color.White.copy(alpha = 0.4f)
                                )
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (uiState.currentViewTab) {
                            0 -> LobbyScreen(state = uiState, viewModel = lobbyViewModel)
                            1 -> ArenaScreen(state = uiState, viewModel = lobbyViewModel)
                            2 -> DevOpsScreen(state = uiState, viewModel = lobbyViewModel)
                            3 -> WebPortalScreen(state = uiState)
                        }
                    }
                }
            }
        }
    }
}
