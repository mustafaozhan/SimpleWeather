package mustafaozhan.github.com.simpleweather.model


/**
 * Created by Mustafa Ozhan on 9/30/17 at 8:11 PM on Arch Linux.
 */
class FutureModel {

    var city: City? = null
    var cod: String? = null
    var message: Double? = null
    var cnt: Int? = null
    var list: List<List<Any?>>? = null
    private val additionalProperties = HashMap<String, Any>()

    fun getAdditionalProperties(): Map<String, Any> = this.additionalProperties

    fun setAdditionalProperty(name: String, value: Any) {
        this.additionalProperties.put(name, value)
    }


    inner class City {

        var id: Int? = null
        var name: String? = null
        var coord: Coord? = null
        var country: String? = null
        var population: Int? = null
        private val additionalProperties = HashMap<String, Any>()

        fun getAdditionalProperties(): Map<String, Any> = this.additionalProperties

        fun setAdditionalProperty(name: String, value: Any) {
            this.additionalProperties.put(name, value)
        }

    }


    inner class Coord {

        var lon: Double? = null
        var lat: Double? = null
        private val additionalProperties = HashMap<String, Any>()

        fun getAdditionalProperties(): Map<String, Any> = this.additionalProperties

        fun setAdditionalProperty(name: String, value: Any) {
            this.additionalProperties.put(name, value)
        }

    }


    inner class List<T> {

        var dt: Int? = null
        var temp: Temp? = null
        var pressure: Double? = null
        var humidity: Int? = null
        var weather: List<Weather>? = null
        var speed: Double? = null
        var deg: Int? = null
        var clouds: Int? = null
        var rain: Double? = null
        private val additionalProperties = HashMap<String, Any>()

        fun getAdditionalProperties(): Map<String, Any> = this.additionalProperties

        fun setAdditionalProperty(name: String, value: Any) {
            this.additionalProperties.put(name, value)
        }

    }

    inner class Temp {

        var day: Double? = null
        var min: Double? = null
        var max: Double? = null
        var night: Double? = null
        var eve: Double? = null
        var morn: Double? = null
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

}