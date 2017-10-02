package mustafaozhan.github.com.simpleweather

import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.location.Location

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import mustafaozhan.github.com.simpleweather.common.Common
import mustafaozhan.github.com.simpleweather.common.Helper
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
                var streamWeather = http.getHTTPData(urlString)
                if (streamWeather.contains("Error: Not found city"))
                    pd.dismiss()
                var gSonWeather = Gson()
                var mType = object : TypeToken<ResponseModel>() {}.type

                openWeatherMap = gSonWeather.fromJson<ResponseModel>(streamWeather, mType)

                streamWeather = http.getHTTPData(futureUrlString)
                if (streamWeather.contains("Error: Not found city"))
                    pd.dismiss()
                gSonWeather = Gson()
                mType = object : TypeToken<FutureModel>() {}.type

                futureOpenWeatherMap = gSonWeather.fromJson<FutureModel>(streamWeather, mType)

                pd.dismiss()

                runOnUiThread {
                    setUi(openWeatherMap)
                    setFutureUi(futureOpenWeatherMap)
                }

            }

        }
    }

    private fun setFutureUi(futureOpenWeatherMap: FutureModel) {

        txtFutureCelsius1.text = futureOpenWeatherMap.list!![0].deg.toString()
        txtFutureCelsius2.text = futureOpenWeatherMap.list!![1].deg.toString()
        txtFutureCelsius3.text = futureOpenWeatherMap.list!![2].deg.toString()
        txtFutureCelsius4.text = futureOpenWeatherMap.list!![3].deg.toString()
        txtFutureCelsius5.text = futureOpenWeatherMap.list!![4].deg.toString()
        txtFutureCelsius6.text = futureOpenWeatherMap.list!![5].deg.toString()
        txtFutureCelsius7.text = futureOpenWeatherMap.list!![6].deg.toString()
        txtFutureCelsius8.text = futureOpenWeatherMap.list!![7].deg.toString()

        imgFuture1.setByUrl(imgFuture1,futureOpenWeatherMap.list!![0].weather!![0].icon!!)
        imgFuture2.setByUrl(imgFuture2,futureOpenWeatherMap.list!![1].weather!![0].icon!!)
        imgFuture3.setByUrl(imgFuture3,futureOpenWeatherMap.list!![2].weather!![0].icon!!)
        imgFuture4.setByUrl(imgFuture4,futureOpenWeatherMap.list!![3].weather!![0].icon!!)
        imgFuture5.setByUrl(imgFuture5,futureOpenWeatherMap.list!![4].weather!![0].icon!!)
        imgFuture6.setByUrl(imgFuture6,futureOpenWeatherMap.list!![5].weather!![0].icon!!)
        imgFuture7.setByUrl(imgFuture7,futureOpenWeatherMap.list!![6].weather!![0].icon!!)
        imgFuture8.setByUrl(imgFuture8,futureOpenWeatherMap.list!![7].weather!![0].icon!!)



//        imgFuture2.setImageByUrl(futureOpenWeatherMap.list!![1].weather!![0].icon!!)
//        imgFuture3.setImageByUrl(futureOpenWeatherMap.list!![2].weather!![0].icon!!)
//        imgFuture4.setImageByUrl(futureOpenWeatherMap.list!![3].weather!![0].icon!!)
//        imgFuture5.setImageByUrl(futureOpenWeatherMap.list!![4].weather!![0].icon!!)
//        imgFuture6.setImageByUrl(futureOpenWeatherMap.list!![5].weather!![0].icon!!)
//        imgFuture7.setImageByUrl(futureOpenWeatherMap.list!![6].weather!![0].icon!!)
//        imgFuture8.setImageByUrl(futureOpenWeatherMap.list!![7].weather!![0].icon!!)

        Log.e("aasdasdas",""+futureOpenWeatherMap.list!![0].weather!![0].icon!!)


    }

    private fun setUi(openWeatherMap: ResponseModel) {
        txtCity.text = "${openWeatherMap.name},${openWeatherMap.sys!!.country}"
        txtLastUpdate.text = "Last Updated: ${Common.getCurrentDate()}"
        txtDescription.text = "${openWeatherMap.weather!![0].description}"
        txtTime.text = "${Common.unixTimeStampToDateTime(openWeatherMap.sys!!.sunrise!!.toDouble())} / ${Common.unixTimeStampToDateTime(openWeatherMap.sys!!.sunset!!.toDouble())}"
        txtHuminty.text = "Humidity %${openWeatherMap.main!!.humidity}"
        txtCelsius.text = "${openWeatherMap.main!!.temp} °C"

        Picasso.with(applicationContext)
                .load(Common.getImage(openWeatherMap.weather!![0].icon!!))
                .into(imageView)


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

private fun ImageView.setByUrl(icon: ImageView, icon1: String) {
    Picasso.with(context)
            .load(Common.getImage(icon1))
            .into(icon)
}


