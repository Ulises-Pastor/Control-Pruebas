package com.example.control_remoto

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import com.example.control_remoto.ui.RemoteControlUI
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RemoteControlInstrumentedTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            RemoteControlUI()
        }
    }

    @Test
    fun testBluetoothButton_clickable() {
        composeTestRule.onNodeWithContentDescription("Bluetooth").performClick()
    }

    @Test
    fun testGiroIzqButton_clickable() {
        composeTestRule.onNodeWithContentDescription("GiroIzq").performClick()
    }

    @Test
    fun testGiroDerButton_clickable() {
        composeTestRule.onNodeWithContentDescription("GiroDer").performClick()
    }

    @Test
    fun testPowerButton_clickable() {
        composeTestRule.onNodeWithContentDescription("Power").performClick()
    }

    @Test
    fun testStopButton_clickable() {
        composeTestRule.onNodeWithContentDescription("Stop").performClick()
    }

    @Test
    fun testLucesButton_clickable() {
        composeTestRule.onNodeWithContentDescription("Luces").performClick()
    }

    @Test
    fun testWiFiButton_clickable() {
        composeTestRule.onNodeWithContentDescription("WiFi").performClick()
    }

    @Test
    fun testArribaButton_clickable() {
        composeTestRule.onNodeWithContentDescription("Arriba").performClick()
    }

    @Test
    fun testAbajoButton_clickable() {
        composeTestRule.onNodeWithContentDescription("Abajo").performClick()
    }
}
