package com.mrhi2024.tpcommunity.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.MapLifeCycleCallback
import com.mrhi2024.tpcommunity.data.KakaoSearch
import com.mrhi2024.tpcommunity.databinding.FragmentMapBinding
import com.mrhi2024.tpcommunity.network.RetrofitHelper
import com.mrhi2024.tpcommunity.network.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class MapFragment : Fragment() {
    private lateinit var binding: FragmentMapBinding

    //1. 검색장소명
    var searchQuery: String = "음식점" //앱 초기 검색어

    //2. 현재 내위치 정보 객체(위도,경도 정보를 멤버로 보유)
    var myLocation: Location? = null

    //[ Google Fused Location API 사용 : play-services-location ]
    val locationProviderClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(
            requireContext()
        )
    }
    
    //3. 보여줄 결과 페이지
    private var pageNumber = 1

    //kakao search API 응답결과 객체 참조변수
    var searchPlaceResponse: KakaoSearch? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //위치정보 제공에 대한 퍼미션 체크
        val permissionState =
            requireContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)

        if (permissionState == PackageManager.PERMISSION_DENIED) {
            //퍼미션을 요청하는 다이얼로그 보이고 그 결과를 받아오는 작업을 대신해주는 대행사 이용
            permissionResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            //위치정보수집이 허가되어 있다면 곧바로 위치정보 얻어오는 작업 시작
//            requestMyLocation()
        }

//        binding.mapView.start(mapLifiCycleCallback, mapReadyCallback)
        binding.btnSearch.setOnClickListener { search() }

    }

    private val permissionResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) requestMyLocation()
            else Toast.makeText(
                requireContext(),
                "내 위치정보를 제공하지 않아 검색기능 사용이 제한됩니다.",
                Toast.LENGTH_SHORT
            ).show()
        }

    //현재 위치를 얻어오는 작업요청 코드가 있는 기능메소드
    private fun requestMyLocation() {

        //요청 객체 생성
        val request: LocationRequest =
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 3000).build()

        //실시간 위치정보 갱신 요청 - 퍼미션 체크코드가 있어야만 함
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationProviderClient.requestLocationUpdates(
            request,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    //위치정보 갱신때마다 발동하는 콜백 객체
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)

            myLocation = p0.lastLocation

            //위치 탐색이 종료되었으니 내 위치 정보 업데이트를 이제 그만
            locationProviderClient.removeLocationUpdates(this) //this: locationCallback 객체

            //위치정보를 얻었으니 키워드 장소검색 작업 시작
//            searchPlaces()
        }
    }


    private val mapLifiCycleCallback = object : MapLifeCycleCallback() {
        override fun onMapDestroy() {

        }

        override fun onMapError(p0: Exception?) {

        }

    }

    private val mapReadyCallback = object : KakaoMapReadyCallback() {
        override fun onMapReady(p0: KakaoMap) {
            Toast.makeText(requireContext(), "fdfd", Toast.LENGTH_SHORT).show()
        }

    }

    private fun search() {
        searchQuery = binding.etSearch.text.toString()

        val retrofit = RetrofitHelper.getRetrofitInstance("https://dapi.kakao.com")
        val retrofitService = retrofit.create(RetrofitService::class.java)
        val call = retrofitService.searchPlace(searchQuery, myLocation?.longitude.toString(), myLocation?.latitude.toString(), pageNumber)
        call.enqueue(object : Callback<KakaoSearch> {
            override fun onResponse(call: Call<KakaoSearch>, response: Response<KakaoSearch>) {
                searchPlaceResponse = response.body()

                AlertDialog.Builder(requireContext()).setMessage("$searchPlaceResponse")
            }

            override fun onFailure(call: Call<KakaoSearch>, t: Throwable) {
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }
}