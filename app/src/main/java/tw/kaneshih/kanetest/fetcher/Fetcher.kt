package tw.kaneshih.kanetest.fetcher

interface Fetcher<T> {
    fun fetch(): T
}