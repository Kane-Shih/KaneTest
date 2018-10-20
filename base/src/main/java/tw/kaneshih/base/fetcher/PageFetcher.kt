package tw.kaneshih.base.fetcher

abstract class PageFetcher<T>(val offset: Int, val count: Int) : Fetcher<T>