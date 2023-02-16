package me.johngachihi.codestats.mobile.android.ui.components

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class UsernameInputDialogTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenIsOpenFalse_dialogNotShown() {
        composeTestRule.setContent {
            UsernameInputDialog(
                isOpen = false,
                onDismiss = { },
                username = "",
                onUsernameChange = { }
            )
        }

        composeTestRule.onNodeWithText("Enter your Codestats username").assertDoesNotExist()
    }

    @Test
    fun inputElementShowsProvidedUsername() {
        composeTestRule.setContent {
            UsernameInputDialog(
                isOpen = true,
                onDismiss = { },
                username = "a-good-username",
                onUsernameChange = { }
            )
        }

        composeTestRule.onNodeWithText("Username").assertTextContains("a-good-username")
    }

    @Test
    fun whenOKButtonClicked_onUsernameChangeCalledWithInputtedUsername_andOnDismissCalled() {
        var username = ""
        val onUsernameChange = { _username: String -> username = _username }

        var onDismissCalled = false
        val onDismiss = { onDismissCalled = true }

        composeTestRule.setContent {
            UsernameInputDialog(
                isOpen = true,
                onDismiss = { onDismiss() },
                username = "",
                onUsernameChange = { onUsernameChange(it) }
            )
        }

        composeTestRule.onNodeWithText("Username").performTextInput("a-username")
        composeTestRule.onNodeWithText("OK").performClick()

        assertEquals("a-username", username)
        assertEquals(true, onDismissCalled)
    }

    @Test
    fun whenCancelButtonClicked_onDismissCalled() {
        var onDismissCalled = false
        val onDismiss = { onDismissCalled = true }

        composeTestRule.setContent {
            UsernameInputDialog(
                isOpen = true,
                onDismiss = { onDismiss() },
                username = "",
                onUsernameChange = { }
            )
        }

        composeTestRule.onNodeWithText("Cancel").performClick()

        assertEquals(true, onDismissCalled)
    }
}