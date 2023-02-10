package me.johngachihi.codestats.intellijplugin

import com.intellij.notification.Notification as IJNotification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.project.Project

data class Notification(val title: String = "Codestats", val content: String)

fun showNotification(project: Project, notification: Notification) {
    Notifications.Bus.notify(
        IJNotification(
            "Codestats",
            notification.title,
            notification.content,
            NotificationType.INFORMATION
        ),
        project
    )
}