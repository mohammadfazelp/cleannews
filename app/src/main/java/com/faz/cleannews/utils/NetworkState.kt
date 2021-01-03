package com.faz.cleannews.utils

data class NetworkState(var status: Status?, var msg: String?) {

    enum class Status {
        RUNNING, SUCCESS, FAILED
    }

    companion object {
        val LOADED = NetworkState(Status.SUCCESS, "Success")
        val LOADING = NetworkState(Status.RUNNING, "Running")
        val FAILED = NetworkState(Status.FAILED, "Failed")
    }
}