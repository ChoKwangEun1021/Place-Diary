package com.mrhi2024.tpcommunity.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.firestore.DocumentSnapshot
import com.google.gson.Gson
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.camera.CameraUpdate
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelLayer
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.mapwidget.InfoWindowOptions
import com.kakao.vectormap.mapwidget.component.GuiLayout
import com.kakao.vectormap.mapwidget.component.GuiText
import com.kakao.vectormap.mapwidget.component.Orientation
import com.mrhi2024.tpcommunity.R
import com.mrhi2024.tpcommunity.activites.BoardDetailActivity
import com.mrhi2024.tpcommunity.activites.BoardWriteActivity
import com.mrhi2024.tpcommunity.activites.PlaceDetailActivity
import com.mrhi2024.tpcommunity.adapter.MapSearchAdapter
import com.mrhi2024.tpcommunity.data.KakaoSearch
import com.mrhi2024.tpcommunity.data.Place
import com.mrhi2024.tpcommunity.data.SearchList
import com.mrhi2024.tpcommunity.databinding.FragmentMapBinding
import com.mrhi2024.tpcommunity.firebase.FBRef
import com.mrhi2024.tpcommunity.network.RetrofitHelper
import com.mrhi2024.tpcommunity.network.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class MapFragment : Fragment(), OnClickListener {
    private lateinit var binding: FragmentMapBinding
    private val bottomSheetBehavior by lazy { BottomSheetBehavior.from(binding.bottomSheet) }
    private val searchList = mutableListOf<SearchList>()

    //1. 검색장소명
    private var searchQuery: String = "" //앱 초기 검색어

    //2. 현재 내위치 정보 객체(위도,경도 정보를 멤버로 보유)
    private var myLocation: Location? = null

    //[ Google Fused Location API 사용 : play-services-location ]
    val locationProviderClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(
            requireContext()
        )
    }

    //3. 보여줄 결과 페이지
    private var pageNumber = 1

    //kakao search API 응답결과 객체 참조변수
    private var searchPlaceResponse: KakaoSearch? = null

    //현재 내 위치를 지도의 중심위치로 설정
    private val latitude: Double = myLocation?.latitude ?: 37.5666
    private val longitude: Double = myLocation?.longitude ?: 126.9782
    private val myPos: LatLng = LatLng.from(latitude, longitude)

    private val adapter by lazy { MapSearchAdapter(requireContext(), searchList) }
    private var isLabelAdded = false
    private val labels = mutableMapOf<String, String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        binding.mapView.start(mapLifiCycleCallback, mapReadyCallback)

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
            requestMyLocation()
        }


        binding.rvList.adapter = adapter

//        binding.btnSearch.setOnClickListener { mapReadyCallback }
        binding.btnPrevPage.setOnClickListener(this)
        binding.btnNextPage.setOnClickListener(this)

        bottomSheet()

    }

    private fun bottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior.peekHeight = 0
        bottomSheetBehavior.isHideable = false

        bottomSheetBehavior.apply {
            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_COLLAPSED -> { //접힘
                        }

                        BottomSheetBehavior.STATE_EXPANDED -> {  //펼쳐짐
                        }

                        BottomSheetBehavior.STATE_HIDDEN -> {}    //숨겨짐
                        BottomSheetBehavior.STATE_HALF_EXPANDED -> {} //절반 펼쳐짐
                        BottomSheetBehavior.STATE_DRAGGING -> {}  //드래그하는 중
                        BottomSheetBehavior.STATE_SETTLING -> {}  //(움직이다가) 안정화되는 중
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    //슬라이드 될때 offset / hide -1.0 ~ collapsed 0.0 ~ expended 1.0

                    binding.bottomSheet.apply {
                        if (slideOffset > 0) {
                            // 슬라이딩 중일 때
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                        } else {
                            // 슬라이딩이 끝났을 때
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                        }
                    }
                }
            })
        }//bottomSheet.apply
    }

    private val permissionResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) requestMyLocation()
            else Toast.makeText(
                requireContext(),
                "내 위치정보를 제공하지 않아 지도기능 사용이 제한됩니다.",
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
//            Toast.makeText(requireContext(), "fdfd", Toast.LENGTH_SHORT).show()

            FBRef.labelRef.get().addOnSuccessListener {
                for (snapShot: DocumentSnapshot in it) {
                    val data = snapShot.data!!
                    val title = data["title"].toString()
                    val latitude = data["latitude"].toString()
                    val longitude = data["longitude"].toString()

                    val pos = LatLng.from(latitude.toDouble(), longitude.toDouble())
                    val options = LabelOptions.from(pos).setStyles(R.drawable.blue_pin)
                        .setTexts(title)
                    p0.labelManager!!.layer!!.addLabel(options)
//                    AlertDialog.Builder(requireContext()).setMessage("$latitude\n$longitude").create().show()
                }
            }

            //내 위치로 지도 카메라 이동
            val cameraUpdate: CameraUpdate = CameraUpdateFactory.newCenterPosition(myPos, 16)
            p0.moveCamera(cameraUpdate)

            //내 위치 마커(라벨) 추가하기
            val labelOptions: LabelOptions =
                LabelOptions.from(myPos).setStyles(R.drawable.ic_mypin) //벡터그래픽 이미지는 안됨
            //라벨이 그려질 레이어 객체 소환
            val labelLayer: LabelLayer = p0.labelManager!!.layer!!
            //라벨 레이어에 라벨 추가
            labelLayer.addLabel(labelOptions)

            binding.btnSearch.setOnClickListener {
                p0.labelManager!!.removeAllLabelLayer()
                val inputMethodManager =
                    activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                searchQuery = binding.etSearch.text.toString()
                pageNumber = 1
                search(searchQuery, pageNumber)

                //주변 검색 장소들에 마커 추가하기
                val placeList: List<Place>? = searchPlaceResponse?.documents
                placeList?.forEach {
                    //마커(라벨)옵션 객체 생성
                    val pos = LatLng.from(it.y.toDouble(), it.x.toDouble())
                    val options = LabelOptions.from(pos).setStyles(R.drawable.ic_pin)
                        .setTexts(it.place_name, it.road_address_name).setTag(it)
                    p0.labelManager!!.layer!!.addLabel(options)
                }

                inputMethodManager.hideSoftInputFromWindow(binding.btnSearch.windowToken, 0)
                bottomSheetBehavior.state =
                    if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) BottomSheetBehavior.STATE_HALF_EXPANDED
                    else BottomSheetBehavior.STATE_COLLAPSED

            }

            binding.btnTarget.setOnClickListener {
                p0.moveCamera(cameraUpdate)
            }

            binding.btnAddMaker.setOnClickListener {


                Toast.makeText(requireContext(), "원하는 위치의 라벨을 추가해주세요!!", Toast.LENGTH_SHORT).show()

                p0.setOnMapClickListener { kakaoMap, latLng, pointF, poi ->
                    if (isLabelAdded) {
                        return@setOnMapClickListener
                    }

                    val intent = Intent(requireContext(), BoardWriteActivity::class.java)

                    intent.putExtra("map", "mapFragment")
                    resultLauncher.launch(intent)

                    val options = LabelOptions.from(latLng).setStyles(R.drawable.blue_pin)
                    p0.labelManager!!.layer!!.addLabel(options)

                    labels["latitude"] = latLng.latitude.toString()
                    labels["longitude"] = latLng.longitude.toString()

                    isLabelAdded = true

                }
                isLabelAdded = false
            }


            //라벨 클릭에 반응하기
            p0.setOnLabelClickListener { kakaoMap, layer, label ->

                label.apply {
                    //정보창 [infoWindow] 보여주기
                    val layout = GuiLayout(Orientation.Vertical)
                    layout.setPadding(16, 16, 16, 16)
                    layout.setBackground(R.drawable.base_msg, true)

                    texts.forEach {
                        val guiText = GuiText(it)
                        guiText.setTextSize(30)
                        guiText.setTextColor(Color.WHITE)
                        layout.addView(guiText)
                    }

                    //정보창 [info window] 객체 만들기
                    val options: InfoWindowOptions = InfoWindowOptions.from(position)
                    options.body = layout
                    options.setBodyOffset(0f, -10f)
                    options.setTag(tag)

                    kakaoMap.mapWidgetManager!!.infoWindowLayer.removeAll()
                    kakaoMap.mapWidgetManager!!.infoWindowLayer.addInfoWindow(options)
                }//apply

            }//label click

            //정보창 클릭에 반응하기
            p0.setOnInfoWindowClickListener { kakaoMap, infoWindow, guiId ->

//                if () {
//                    startActivity(Intent(requireContext(), BoardDetailActivity::class.java))
//                }

                //장소에 대한 상세 소개 웹페이지를 보여주는 화면으로 이동
                val intent = Intent(requireContext(), PlaceDetailActivity::class.java)

                //클릭한 장소에 대한 정보를 Json문자열로 변환하여 전달해주기
                val place: Place = infoWindow.tag as Place

                val json: String = Gson().toJson(place)

                intent.putExtra("place", json)
                startActivity(intent)
            }

            adapter.setOnItemClickListner(object : MapSearchAdapter.OnItemClickListner {
                override fun onItemClick(view: View?, position: Int) {
                    val labelPos = LatLng.from(
                        searchList[position].y,
                        searchList[position].x
                    )
                    val cameraUpdate2: CameraUpdate =
                        CameraUpdateFactory.newCenterPosition(labelPos, 16)
                    p0.moveCamera(cameraUpdate2)
                }

            })

        }

    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {

                val title = it.data?.getStringExtra("title")

                labels["title"] = title.toString()

                FBRef.labelRef.document().set(labels).addOnSuccessListener {
                    Toast.makeText(requireContext(), "라벨 추가가 완료되었습니다!!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

    private fun search(query: String, page: Int) {
        searchQuery = binding.etSearch.text.toString()

        val retrofit = RetrofitHelper.getRetrofitInstance("https://dapi.kakao.com")
        val retrofitService = retrofit.create(RetrofitService::class.java)
        val call = retrofitService.searchPlace(searchQuery, pageNumber)
        call.enqueue(object : Callback<KakaoSearch> {
            override fun onResponse(call: Call<KakaoSearch>, response: Response<KakaoSearch>) {
                searchPlaceResponse = response.body()
                addLabels(searchPlaceResponse)

//                AlertDialog.Builder(requireContext()).setMessage("${searchPlaceResponse}").create().show()
            }

            override fun onFailure(call: Call<KakaoSearch>, t: Throwable) {
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
            }

        })

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addLabels(response: KakaoSearch?) {
        if (response?.documents.isNullOrEmpty()) {
            binding.rvList.visibility = View.GONE
            Toast.makeText(requireContext(), "검색결과 없음", Toast.LENGTH_SHORT).show()
        } else {
            binding.rvList.visibility = View.VISIBLE
            searchList.clear()

            for (data in response!!.documents) {
                searchList.add(
                    SearchList(
                        data.place_name,
                        data.road_address_name,
                        data.address_name,
                        data.x.toDouble(),
                        data.y.toDouble()
                    )
                )
            }

            binding.rvList.adapter!!.notifyDataSetChanged()
            binding.btnNextPage.isEnabled = !response.meta.is_end // 페이지가 더 있을 경우 다음 버튼 활성화
            binding.btnPrevPage.isEnabled = pageNumber != 1             // 1페이지가 아닐 경우 이전 버튼 활성화

        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
//            R.id.btn_search -> {
//                val inputMethodManager =
//                    activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                searchQuery = binding.etSearch.text.toString()
//                pageNumber = 1
//                search(searchQuery, pageNumber)
//                inputMethodManager.hideSoftInputFromWindow(binding.btnSearch.windowToken, 0)
//                bottomSheetBehavior.state =
//                    if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) BottomSheetBehavior.STATE_HALF_EXPANDED
//                    else BottomSheetBehavior.STATE_COLLAPSED
//            }

            R.id.btn_prevPage -> {
                pageNumber--
                binding.tvPageNumber.text = pageNumber.toString()
                search(searchQuery, pageNumber)
            }

            R.id.btn_nextPage -> {
                pageNumber++
                binding.tvPageNumber.text = pageNumber.toString()
                search(searchQuery, pageNumber)

            }

        }//when
    }
}