package com.gogoh5.apps.quanmaomao.library.base

abstract class BaseMethodPresenter<T: IView, E: BaseMethod<*>>(view: T) : BasePresenter<T>(view) {
    protected val method: E by lazy {generateMethod()}

    /**
     * Generate method object
     */
    abstract fun generateMethod(): E

    /**
     * Rewrite destroy method for method object
     */
    override fun viewDestroy() {
        method.destroy()
        super.viewDestroy()
    }
}