package team_emergensor.co.jp.emergensor.lib.filter

import io.reactivex.Observable


sealed class Filter {
    abstract class OneToOne<in T, out O>: Filter() {
        abstract fun filter(message: T): O
    }

    abstract class OneToMany<in T, O>: Filter() {
        abstract fun filter(message: T): Observable<O>
    }
}