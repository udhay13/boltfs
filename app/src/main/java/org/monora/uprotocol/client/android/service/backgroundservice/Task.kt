package org.monora.uprotocol.client.android.service.backgroundservice

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.Job

class Task(val name: String, val params: Any, val job: Job, state: LiveData<State>) {
    val state = liveData {
        emitSource(state)
    }

    sealed class State(val running: Boolean = false) {
        object Pending : State()

        class Running(val message: String) : State(running = true)

        class Progress(val message: String, val total: Int, val progress: Int) : State(running = true)

        class Error(val error: Exception): State()

        object Finished : State()
    }

    data class Change<T>(val task: Task, val exported: T, val state: State)
}