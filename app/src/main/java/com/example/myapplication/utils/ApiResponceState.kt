package com.example.myapplication.utils


sealed class ApiResponceState<T> (val myData : T?= null, val message : String?= null) {

    class SuccessState<T>(data : T) : ApiResponceState<T>(data)

    class ErrorState<T>(data : T?= null, errorMessage : String) : ApiResponceState<T>(data, errorMessage)

    class InProcessState<T> : ApiResponceState<T>()

}
