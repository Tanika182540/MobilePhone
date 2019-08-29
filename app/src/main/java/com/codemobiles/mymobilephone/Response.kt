package com.codemobiles.mymobilephone

import org.json.JSONObject

class Response(json: String) : JSONObject(json) {
    val type: String? = this.optString("type")
    val data = this.optJSONArray("data")
            ?.let { 0.until(it.length()).map { i -> it.optJSONObject(i) } } // returns an array of JSONObject
            ?.map { Foo(it.toString()) } // transforms each JSONObject of the array into Foo
}

class Foo(json: String) : JSONObject(json) {
    val id = this.optInt("id")
    val brand: String? = this.optString("brand")

}