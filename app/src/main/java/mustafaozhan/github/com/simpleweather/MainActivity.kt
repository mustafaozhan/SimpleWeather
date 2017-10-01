package mustafaozhan.github.com.simpleweather

import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.location.Location

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
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
import mustafaozhan.github.com.simpleweather.common.extension.setImageByUrl
import mustafaozhan.github.com.simpleweather.model.FutureModel
import mustafaozhan.github.com.simpleweather.model.ResponseModel
import org.jetbrains.anko.doAsync
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {


    private val PERMISSION_REQUEST_CODE = 1001
    private val PLAY_SERVICE_RESOLUTION_REQUEST = 1000

    private var mGoogleApiClient: GoogleApiClient? = null
    var mLocationRequest: LocationRequest? = null
    var openWeatherMap = ResponseModel()
    var futureOpenWeatherMap = FutureModel()

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
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
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

        val pd = ProgressDialog(this@MainActivity)
        pd.setTitle("Please wait")
        pd.show()

        doAsync {

            val urlString = Common.apiRequest(location?.latitude.toString(), location?.longitude.toString())
            val futureUrlString = Common.futureApiRequest(location?.latitude.toString(), location?.longitude.toString())
            val http = Helper()


            thread {
                var stream = http.getHTTPData(urlString)
                if (stream.contains("Error: Not found city"))
                    pd.dismiss()
                var gson = Gson()
                var mType = object : TypeToken<ResponseModel>() {}.type

                openWeatherMap = gson.fromJson<ResponseModel>(stream, mType)


                stream = http.getHTTPData(futureUrlString)
                if (stream.contains("Error: Not found city"))
                    pd.dismiss()
                gson = Gson()
                mType = object : TypeToken<FutureModel>() {}.type

                futureOpenWeatherMap = gson.fromJson<FutureModel>(stream, mType)

                val a=futureOpenWeatherMap

                pd.dismiss()

                runOnUiThread {
                    setUi(openWeatherMap)
                    setFutureUi(futureOpenWeatherMap)
                }

            }

        }
    }

    private fun setFutureUi(futureOpenWeatherMap: FutureModel) {



    }

    private fun setUi(openWeatherMap: ResponseModel) {
        txtCity.text = "${openWeatherMap.name},${openWeatherMap.sys!!.country}"
        txtLastUpdate.text = "Last Updated: ${Common.getCurrentDate()}"
        txtDescription.text = "${openWeatherMap.weather!![0].description}"
        txtTime.text = "${Common.unixTimeStampToDateTime(openWeatherMap.sys!!.sunrise!!.toDouble())} / ${Common.unixTimeStampToDateTime(openWeatherMap.sys!!.sunset!!.toDouble())}"
        txtHuminty.text = "Humidity %${openWeatherMap.main!!.humidity}"
        txtCelsius.text = "${openWeatherMap.main!!.temp} °C"

        imageView.setImageByUrl(openWeatherMap.weather!![0].icon!!)

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


