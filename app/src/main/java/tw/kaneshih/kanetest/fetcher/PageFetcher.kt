package tw.kaneshih.kanetest.fetcher

abstract class PageFetcher<T>(val offset: Int, val count: Int) : Fetcher<T>