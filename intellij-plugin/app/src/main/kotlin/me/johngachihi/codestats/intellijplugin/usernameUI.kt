package me.johngachihi.codestats.intellijplugin

import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidget.TextPresentation
import com.intellij.openapi.wm.StatusBarWidgetFactory
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.Consumer
import com.intellij.util.ui.FormBuilder
import java.awt.Component
import java.awt.event.MouseEvent
import javax.swing.JPanel


class CodestatsStatusBarWidgetFactory : StatusBarWidgetFactory {
    override fun getId() = "me.johngachihi.codestats.CodestatsStatusBarWidgetFactory"
    override fun getDisplayName() = "Codestats"
    override fun isAvailable(project: Project) = true
    override fun createWidget(project: Project) = CodestatsStatusBarWidget(project)
    override fun canBeEnabledOn(statusBar: StatusBar) = true
    override fun disposeWidget(widget: StatusBarWidget) {}
}

class CodestatsStatusBarWidget(val project: Project) : StatusBarWidget, TextPresentation {
    override fun ID() = "me.johngachihi.codestats.CodestatsStatusBarWidget"

    override fun getPresentation() = this
    override fun getText() = "CS"
    override fun getAlignment() = Component.CENTER_ALIGNMENT
    override fun getTooltipText() = "Codestats"

    override fun getClickConsumer() = Consumer<MouseEvent> {
        CommandProcessor.getInstance().executeCommand(
            project,
            { CodestatsUsernameDialog(project).show() },
            "Set Codestats Username",
            null
        )
    }

    override fun install(statusBar: StatusBar) {}
    override fun dispose() {}
}

class CodestatsUsernameDialog(private val project: Project) : DialogWrapper(project) {
    private val usernameTxt = JBTextField(CodestatsPreferences.getInstance().username, 20)

    private var mainPanel: JPanel? = FormBuilder.createFormBuilder()
        .addVerticalGap(6)
        .addLabeledComponent(JBLabel("Insert your Codestats username:"), usernameTxt, true)
        .addVerticalGap(2)
        .panel

    init {
        title = "Codestats"
        init()
    }

    override fun doOKAction() {
        CodestatsPreferences.getInstance().username = usernameTxt.text.ifEmpty { null }
        showNotification(
            project,
            Notification(content = "Successfully updated username")
        )
        super.doOKAction()
    }

    override fun createCenterPanel() = mainPanel
}
