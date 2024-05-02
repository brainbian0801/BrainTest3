package com.example.brainaidlserver

import android.os.Parcel
import android.os.Parcelable
import android.util.Log

class Person(var name: String? = ""): Parcelable {

    constructor(parcel: Parcel) : this(parcel.readString())

    override fun toString(): String {
        return "Person(name=$name) hashCode = ${hashCode()}"
    }

    override fun writeToParcel(parcel: Parcel, flag: Int) {
        Log.i("Brain", "Person.kt writeToParcel..........")
        parcel.writeString(name)
    }

    fun readFromParcel(parcel: Parcel) {
        Log.i("Brain", "Person.kt readFromParcel...........")
        this.name = parcel.readString()
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<Person> {

        override fun createFromParcel(parcel: Parcel): Person {
            return Person(parcel)
        }

        override fun newArray(size: Int): Array<Person?> {
            return arrayOfNulls(size)
        }
    }

}