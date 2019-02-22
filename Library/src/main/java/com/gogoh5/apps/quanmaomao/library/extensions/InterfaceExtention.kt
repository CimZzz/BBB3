package com.gogoh5.apps.quanmaomao.library.extensions

import okhttp3.Request

typealias Run = () -> Unit
typealias TakeRun<T> = (T) -> Unit
typealias TakeRunMoreResultMore<T, E> = (T?, E?) -> Array<out Any>
typealias TakeRunResult<T, E> = (T?) -> E?
typealias TakeRunResultMore<T> = (T?) -> Array<out Any>
typealias TakeRunMoreResult<T, E> = (T?, E?) -> Any?
typealias TakeMoreRun<T, E> = (T, E) -> Unit
typealias TakeRunAnyResult<T> = (T?) -> Any?
typealias TakeManyRun = (Array<Any>) -> Unit

//-------Base Request
typealias RequestBuilderHooker = TakeRun<Request.Builder>
typealias SuccessResponseHooker<T> = TakeRunMoreResultMore<T, Any?>
typealias FailureResponseHooker = TakeRunResultMore<Any?>
typealias GetResultNotNull<T> = () -> T


//-------PageChangeListener
typealias PageScrollStateChanged = TakeRun<Int>
typealias PageSelected = TakeRun<Int>
typealias PageScrolled = (position: Int, positionOffset: Float, positionOffsetPixels: Int) -> Unit

//-------ScrollChangeListener
typealias ScrollChanged = (Int, Int, Int, Int) -> Unit

//-------RefDataHunter
typealias DataHunterRun<T> = (T, Array<out Any>) -> Unit