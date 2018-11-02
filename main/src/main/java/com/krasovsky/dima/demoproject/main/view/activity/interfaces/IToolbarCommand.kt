package com.krasovsky.dima.demoproject.main.view.activity.interfaces

const val COMMAND_BACK = 0
const val COMMAND_CANCEL = 1
interface IToolbarCommand {
    fun sendCommand(command: Int)
}