package ro.pub.cs.systems.eim.practicaltest02v8

import com.google.gson.annotations.SerializedName


data class BitcoinData (

    @SerializedName("time"       ) var time       : Time?   = Time(),
    @SerializedName("disclaimer" ) var disclaimer : String? = null,
    @SerializedName("bpi"        ) var bpi        : Bpi?    = Bpi()

)

data class Time (

    @SerializedName("updated"    ) var updated    : String? = null,
    @SerializedName("updatedISO" ) var updatedISO : String? = null,
    @SerializedName("updateduk"  ) var updateduk  : String? = null
)

data class USD (

    @SerializedName("code"        ) var code        : String? = null,
    @SerializedName("rate"        ) var rate        : String? = null,
    @SerializedName("description" ) var description : String? = null,
    @SerializedName("rate_float"  ) var rateFloat   : Double? = null
)

data class EUR (

    @SerializedName("code"        ) var code        : String? = null,
    @SerializedName("rate"        ) var rate        : String? = null,
    @SerializedName("description" ) var description : String? = null,
    @SerializedName("rate_float"  ) var rateFloat   : Double? = null
)

data class Bpi (

    @SerializedName("USD" ) var USD : USD? = USD(),
    @SerializedName("EUR" ) var EUR : EUR? = EUR()

)