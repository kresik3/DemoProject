package com.krasovsky.dima.demoproject.main.command.manager

import android.os.AsyncTask.execute
import android.support.v4.app.Fragment
import com.krasovsky.dima.demoproject.main.command.interfaces.ActionActivityCommand
import com.krasovsky.dima.demoproject.main.command.interfaces.ActionFragmentCommand


class ActionCommandManager {
    fun executeCommand(command: ActionFragmentCommand): Fragment {
        return command.execute()
    }

    fun executeCommand(command: ActionActivityCommand) {
        command.execute()
    }
}