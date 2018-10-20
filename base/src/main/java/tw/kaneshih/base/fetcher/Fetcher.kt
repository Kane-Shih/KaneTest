package tw.kaneshih.base.fetcher

interface Fetcher<T> {
    fun fetch(): T
}