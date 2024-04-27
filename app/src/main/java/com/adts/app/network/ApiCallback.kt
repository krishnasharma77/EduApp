package com.adts.app.network

/**
 * Created by Logictrix on 22-Oct-21.
 */
interface ApiCallback {
    /**
     * Method for getting the type and data.
     *
     * @param data Actual data
     */
    fun onSuccess( type:String,data: Any?)

    /**
     * Failure Reason
     * @param data
     */
    fun onFailure(data: Any?)
}