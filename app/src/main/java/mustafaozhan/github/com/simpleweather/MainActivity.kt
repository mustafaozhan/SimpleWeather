package mustafaozhan.github.com.simpleweather

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import mustafaozhan.github.com.simpleweather.common.Common
import mustafaozhan.github.com.simpleweather.common.Helper
import mustafaozhan.github.com.simpleweather.extension.setFromDrawableByName
import mustafaozhan.github.com.simpleweather.model.FutureModel
import mustafaozhan.github.com.simpleweather.model.ResponseModel
import org.jetbrains.anko.doAsync
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {


    private val PERMISSION_REQUEST_CODE = 1001
    private val PLAY_SERVICE_RESOLUTION_REQUEST = 1000

    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLocationRequest: LocationRequest? = null
    private var openWeatherMap = ResponseModel()
    private var futureOpenWeatherMap = FutureModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermission()
        if (checkPlayService())
            buildGoogleApiClient()
    }

    private fun requestPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_CODE)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    if (checkPlayService())
                        buildGoogleApiClient()
            }
        }
    }

    private fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build()
    }


    @Suppress("DEPRECATION")
    private fun checkPlayService(): Boolean {
        val resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICE_RESOLUTION_REQUEST).show()
            else {
                Toast.makeText(applicationContext,
                        "This device is not supported",
                        Toast.LENGTH_SHORT).show()
                finish()
            }
            return false
        }
        return true
    }

    override fun onConnected(p0: Bundle?) {
        createLocationRequest()
    }

    private fun createLocationRequest() {
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = 10000
        mLocationRequest!!.fastestInterval = 5000
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this)


    }

    override fun onConnectionSuspended(p0: Int) {
        mGoogleApiClient!!.connect()
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.i("ERROR", "Connection failed: " + p0.errorCode)
    }

    override fun onLocationChanged(location: Location?) {


        doAsync {

            mProgressBar.progress
            val urlString = Common.apiRequest(location?.latitude.toString(), location?.longitude.toString())
            val futureUrlString = Common.futureApiRequest(location?.latitude.toString(), location?.longitude.toString())
            val http = Helper()

            thread {
                var streamWeather = http.getHTTPData(urlString)

                var gSonWeather = Gson()
                var mType = object : TypeToken<ResponseModel>() {}.type

                openWeatherMap = gSonWeather.fromJson<ResponseModel>(streamWeather, mType)

                streamWeather = http.getHTTPData(futureUrlString)

                gSonWeather = Gson()
                mType = object : TypeToken<FutureModel>() {}.type

                futureOpenWeatherMap = gSonWeather.fromJson<FutureModel>(streamWeather, mType)

                runOnUiThread {
                    setUi(openWeatherMap)
                    setFutureUi(futureOpenWeatherMap)
                    mProgressBar.visibility=View.GONE
                }

            }

        }
    }

    @SuppressLint("SetTextI18n")
    private fun setFutureUi(futureOpenWeatherMap: FutureModel) {

        txtFutureDay1.text = "Day: ${futureOpenWeatherMap.list!![0].temp!!.day!!.toInt()} °C"
        txtFutureDay2.text = "Day: ${futureOpenWeatherMap.list!![1].temp!!.day!!.toInt()} °C"
        txtFutureDay3.text = "Day: ${futureOpenWeatherMap.list!![2].temp!!.day!!.toInt()} °C"
        txtFutureDay4.text = "Day: ${futureOpenWeatherMap.list!![3].temp!!.day!!.toInt()} °C"
        txtFutureDay5.text = "Day: ${futureOpenWeatherMap.list!![4].temp!!.day!!.toInt()} °C"
        txtFutureDay6.text = "Day: ${futureOpenWeatherMap.list!![5].temp!!.day!!.toInt()} °C"
        txtFutureDay7.text = "Day: ${futureOpenWeatherMap.list!![6].temp!!.day!!.toInt()} °C"
        txtFutureDay8.text = "Day: ${futureOpenWeatherMap.list!![7].temp!!.day!!.toInt()} °C"

        txtFutureNight1.text = "Night: ${futureOpenWeatherMap.list!![0].temp!!.night!!.toInt()} °C"
        txtFutureNight2.text = "Night: ${futureOpenWeatherMap.list!![1].temp!!.night!!.toInt()} °C"
        txtFutureNight3.text = "Night: ${futureOpenWeatherMap.list!![2].temp!!.night!!.toInt()} °C"
        txtFutureNight4.text = "Night: ${futureOpenWeatherMap.list!![3].temp!!.night!!.toInt()} °C"
        txtFutureNight5.text = "Night: ${futureOpenWeatherMap.list!![4].temp!!.night!!.toInt()} °C"
        txtFutureNight6.text = "Night: ${futureOpenWeatherMap.list!![5].temp!!.night!!.toInt()} °C"
        txtFutureNight7.text = "Night: ${futureOpenWeatherMap.list!![6].temp!!.night!!.toInt()} °C"
        txtFutureNight8.text = "Night: ${futureOpenWeatherMap.list!![7].temp!!.night!!.toInt()} °C"

        txtDate1.text = Common.getCurrentDate(0)
        txtDate2.text = Common.getCurrentDate(1)
        txtDate3.text = Common.getCurrentDate(2)
        txtDate4.text = Common.getCurrentDate(3)
        txtDate5.text = Common.getCurrentDate(4)
        txtDate6.text = Common.getCurrentDate(5)
        txtDate7.text = Common.getCurrentDate(6)
        txtDate8.text = Common.getCurrentDate(7)

        imgFuture1.setFromDrawableByName(futureOpenWeatherMap.list!![0].weather!![0].icon!!)
        imgFuture2.setFromDrawableByName(futureOpenWeatherMap.list!![1].weather!![0].icon!!)
        imgFuture3.setFromDrawableByName(futureOpenWeatherMap.list!![2].weather!![0].icon!!)
        imgFuture4.setFromDrawableByName(futureOpenWeatherMap.list!![3].weather!![0].icon!!)
        imgFuture5.setFromDrawableByName(futureOpenWeatherMap.list!![4].weather!![0].icon!!)
        imgFuture6.setFromDrawableByName(futureOpenWeatherMap.list!![5].weather!![0].icon!!)
        imgFuture7.setFromDrawableByName(futureOpenWeatherMap.list!![6].weather!![0].icon!!)
        imgFuture8.setFromDrawableByName(futureOpenWeatherMap.list!![7].weather!![0].icon!!)


    }

    @SuppressLint("SetTextI18n")
    private fun setUi(openWeatherMap: ResponseModel) {
        txtCity.text = "${openWeatherMap.name},${openWeatherMap.sys!!.country}"
        txtLastUpdate.text = "Last Updated: ${Common.getCurrentDateAndTime()}"
        txtDescription.text = "${openWeatherMap.weather!![0].description}"
        txtTime.text = "${Common.unixTimeStampToDateTime(openWeatherMap.sys!!.sunrise!!.toDouble())} / ${Common.unixTimeStampToDateTime(openWeatherMap.sys!!.sunset!!.toDouble())}"
        txtHuminty.text = "Humidity %${openWeatherMap.main!!.humidity}"
        txtCelsius.text = "${openWeatherMap.main!!.temp} °C"
        imageView.setFromDrawableByName(openWeatherMap.weather!![0].icon!!)
    }

    override fun onStart() {
        super.onStart()
        if (mGoogleApiClient != null)
            mGoogleApiClient!!.connect()
    }

    override fun onDestroy() {
        mGoogleApiClient!!.disconnect()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        checkPlayService()
    }
}


