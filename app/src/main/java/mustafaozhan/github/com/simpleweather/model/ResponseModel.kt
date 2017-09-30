package mustafaozhan.github.com.simpleweather.model


/**
 * Created by Mustafa Ozhan on 9/30/17 at 2:03 PM on Arch Linux.
 */
class ResponseModel {
    var coord: Coord? = null
    var weather: List<Weather>? = null
    var base: String? = null
    var main: Main? = null
    var wind: Wind? = null
    var clouds: Clouds? = null
    var dt: Int? = null
    var sys: Sys? = null
    var id: Int? = null
    var name: String? = null
    var cod: Int? = null
    private val additionalProperties = HashMap<String, Any>()

    constructor() {}
    constructor(coord: Coord, weatherList: List<Weather>, base: String, main: Main, wind: Wind, clouds: Clouds, dt: Int, sys: Sys, id: Int, name: String, cod: Int) {
        this.coord = coord
        this.weather = weatherList
        this.base = base
        this.main = main
        this.wind = wind
        this.clouds = clouds
        this.dt = dt
        this.sys = sys
        this.id = id
        this.name = name
        this.cod = cod
    }

    fun getAdditionalProperties(): Map<String, Any> = this.additionalProperties

    fun setAdditionalProperty(name: String, value: Any) {
        this.additionalProperties.put(name, value)
    }


    inner class Clouds {

        var all: Int? = null
        private val additionalProperties = HashMap<String, Any>()

        fun getAdditionalProperties(): Map<String, Any> = this.additionalProperties

        fun setAdditionalProperty(name: String, value: Any) {
            this.additionalProperties.put(name, value)
        }

    }

    inner class Wind {

        var speed: Double? = null
        var deg: Double? = null
        private val additionalProperties = HashMap<String, Any>()

        fun getAdditionalProperties(): Map<String, Any> = this.additionalProperties

        fun setAdditionalProperty(name: String, value: Any) {
            this.additionalProperties.put(name, value)
        }

    }

    inner class Weather {

        var id: Int? = null
        var main: String? = null
        var description: String? = null
        var icon: String? = null
        private val additionalProperties = HashMap<String, Any>()

        fun getAdditionalProperties(): Map<String, Any> = this.additionalProperties

        fun setAdditionalProperty(name: String, value: Any) {
            this.additionalProperties.put(name, value)
        }

    }

    inner class Sys {

        var message: Double? = null
        var country: String? = null
        var sunrise: Int? = null
        var sunset: Int? = null
        private val additionalProperties = HashMap<String, Any>()

        fun getAdditionalProperties(): Map<String, Any> = this.additionalProperties

        fun setAdditionalProperty(name: String, value: Any) {
            this.additionalProperties.put(name, value)
        }

    }


    inner class Main {

        var temp: Double? = null
        var pressure: Double? = null
        var humidity: Int? = null
        var tempMin: Double? = null
        var tempMax: Double? = null
        var seaLevel: Double? = null
        var grndLevel: Double? = null
        private val additionalProperties = HashMap<String, Any>()

        fun getAdditionalProperties(): Map<String, Any> = this.additionalProperties

        fun setAdditionalProperty(name: String, value: Any) {
            this.additionalProperties.put(name, value)
        }

    }

    inner class Coord {

        var lon: Int? = null
        var lat: Int? = null
        private val additionalProperties = HashMap<String, Any>()

        fun getAdditionalProperties(): Map<String, Any> = this.additionalProperties

        fun setAdditionalProperty(name: String, value: Any) {
            this.additionalProperties.put(name, value)
        }

    }


}