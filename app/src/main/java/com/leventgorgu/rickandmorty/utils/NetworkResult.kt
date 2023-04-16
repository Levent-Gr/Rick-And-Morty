package com.leventgorgu.rickandmorty.utils

data class NetworkResult<out T>(var status: Status, val data: T?, val message: String?){
    companion object {
        fun <T> success(data: T?): NetworkResult<T> {
            return NetworkResult(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): NetworkResult<T> {
            return NetworkResult(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): NetworkResult<T> {
            return NetworkResult(Status.LOADING, data, null)
        }
    }
}