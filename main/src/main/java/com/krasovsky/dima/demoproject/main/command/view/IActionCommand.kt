package com.krasovsky.dima.demoproject.main.command.view

import com.krasovsky.dima.demoproject.main.command.interfaces.ActionActivityCommand
import com.krasovsky.dima.demoproject.main.command.interfaces.ActionBudgeCommand
import com.krasovsky.dima.demoproject.main.command.interfaces.ActionFragmentCommand



interface IActionCommand {
    fun sendCommand(command: ActionFragmentCommand)
    fun sendCommand(command: ActionActivityCommand)
    fun sendCommand(command: ActionBudgeCommand)
}