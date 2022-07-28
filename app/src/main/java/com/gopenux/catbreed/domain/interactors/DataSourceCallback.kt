package com.gopenux.catbreed.domain.interactors

interface DataSourceCallback<P> {
    fun onSuccess(result: P)
    fun onFail(e: Exception)
}