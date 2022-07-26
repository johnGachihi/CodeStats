package me.johngachihi.codestats.intellijplugin

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.actionSystem.ex.AnActionListener
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.actions.PasteAction
import com.intellij.openapi.ide.CopyPasteManager
import me.johngachihi.codestats.core.CodingEvent
import me.johngachihi.codestats.core.CodingEventType
import java.awt.datatransfer.DataFlavor
import java.time.Instant


internal class EditorTypingActionListener : AnActionListener {
    private val logger = service<RemoteLogger>()

    @Suppress("DeferredResultUnused")
    override fun beforeEditorTyping(c: Char, dataContext: DataContext) {
        logger.logAsync(CodingEvent(CodingEventType.CHAR_TYPED, c.toString(), Instant.now()))
    }

    @Suppress("DeferredResultUnused")
    override fun beforeActionPerformed(action: AnAction, dataContext: DataContext, event: AnActionEvent) {
        if (action is PasteAction) {
            val pastedText: String = CopyPasteManager.getInstance().getContents(DataFlavor.stringFlavor)!!
            logger.logAsync(CodingEvent(CodingEventType.PASTE, pastedText, Instant.now()))
        }
    }
}