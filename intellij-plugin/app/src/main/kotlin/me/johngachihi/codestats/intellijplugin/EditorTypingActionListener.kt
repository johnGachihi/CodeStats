package me.johngachihi.codestats.intellijplugin

import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.actionSystem.ex.AnActionListener
import com.intellij.openapi.components.service
import me.johngachihi.codestats.core.CodingEvent
import me.johngachihi.codestats.core.CodingEventType
import java.time.Instant


internal class EditorTypingActionListener : AnActionListener {
    private val logger = service<Logger>()

    override fun beforeEditorTyping(c: Char, dataContext: DataContext) {
        logger.log(CodingEvent(CodingEventType.CHAR_TYPED, c.toString(), Instant.now()))
    }
}