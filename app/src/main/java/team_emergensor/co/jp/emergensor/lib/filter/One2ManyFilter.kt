package team_emergensor.co.jp.emergensor.lib.filter

import android.util.Log
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import team_emergensor.co.jp.emergensor.Entity.Message


abstract class One2ManyFilter {

    private var source: Observable<Message>? = null
    private var subject = PublishSubject.create<Message>()

    abstract fun filtering(message: Message)

    fun setObservableSource(observable: Observable<Message>): Observable<Message> {
        source = observable
        startFiltering()
        return subject
    }

    private fun startFiltering() {
        source?.subscribe { filtering(it) }
    }

    fun publish(message: Message) {
        subject.onNext(message)
    }

    fun log(tag: String, content: String) {
        Log.d(tag, content)
    }
}