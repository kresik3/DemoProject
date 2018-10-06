package com.krasovsky.dima.demoproject.main.command.view

import com.krasovsky.dima.demoproject.main.command.interfaces.ActionFragmentCommand



interface IActionCommand {
    fun sendCommand(command: ActionFragmentCommand)
}