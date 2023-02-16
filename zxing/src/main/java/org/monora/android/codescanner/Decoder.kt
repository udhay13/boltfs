package org.monora.android.codescanner

import android.os.Process
import com.google.zxing.BarcodeFormat
import com.google.zxing.DecodeHintType
import com.google.zxing.MultiFormatReader
import com.google.zxing.ReaderException
import com.google.zxing.Result
import java.util.*
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class Decoder(
    private val stateListener: StateListener,
    formats: List<BarcodeFormat?>,
    @Volatile var decodeCallback: DecodeCallback?,
) {
    private val reader: MultiFormatReader = MultiFormatReader()

    private val decoderThread: DecoderThread = DecoderThread()

    private val hints: MutableMap<DecodeHintType, Any?>

    private val lock = ReentrantLock()

    private val lockDecodeCondition = lock.newCondition()

    @Volatile
    private var task: DecodeTask? = null

    @Volatile
    var decoderState: State = State.INITIALIZED
        private set

    fun decode(task: DecodeTask) {
        lock.withLock {
            if (decoderState != State.STOPPED) {
                this.task = task
                lockDecodeCondition.signal()
            }
        }
    }

    fun setFormats(formats: List<BarcodeFormat?>) {
        hints[DecodeHintType.POSSIBLE_FORMATS] = formats
        reader.setHints(hints)
    }

    fun setDecoderState(state: State): Boolean {
        decoderState = state
        return stateListener.onStateChanged(state)
    }

    fun start() {
        check(decoderState == State.INITIALIZED) { "Illegal decoder state" }
        decoderThread.start()
    }

    fun shutdown() {
        decoderThread.interrupt()
        task = null
    }

    private inner class DecoderThread : Thread("cs-decoder") {
        override fun run() {
            // TODO: 3/19/21 Use Handler() instead?
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND)
            mainLoop@ while (true) {
                setDecoderState(Decoder.State.IDLE)
                var result: Result? = null

                try {
                    var task: DecodeTask

                    while (true) {
                        val t = this@Decoder.task
                        if (t != null) {
                            this@Decoder.task = null
                            task = t
                            break
                        }
                        try {
                            lock.withLock {
                                lockDecodeCondition.await()
                            }
                        } catch (e: InterruptedException) {
                            setDecoderState(Decoder.State.STOPPED)
                            break@mainLoop
                        }
                    }
                    setDecoderState(Decoder.State.DECODING)
                    result = task.decode(reader)
                } catch (ignored: ReaderException) {
                } finally {
                    if (result != null) {
                        task = null
                        if (setDecoderState(Decoder.State.DECODED)) {
                            val callback = decodeCallback
                            callback?.onDecoded(result)
                        }
                    }
                }
            }
        }
    }

    interface StateListener {
        fun onStateChanged(state: State): Boolean
    }

    enum class State {
        INITIALIZED, IDLE, DECODING, DECODED, STOPPED
    }

    init {
        hints = EnumMap(DecodeHintType::class.java)
        hints[DecodeHintType.POSSIBLE_FORMATS] = formats
        reader.setHints(hints)
    }
}