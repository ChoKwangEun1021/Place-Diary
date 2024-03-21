package com.mrhi2024.tpcommunity.data

data class KakaoSearch(var meta: PlaceMeta, var documents: List<Place>)
data class PlaceMeta(var total_count: Int, var pageable_count: Int, var is_end: Boolean)
data class Place(
    var id: String,
    var place_name: String,
    var category_name: String,
    var phone: String,
    var address_name: String,
    var road_address_name: String,
    var x: String,
    var y: String,
    var place_url: String,
    var distance: String
)

data class SearchList(
    val name: String,      // 장소명
    val road: String,      // 도로명 주소
    val address: String,   // 지번 주소
    val x: Double,         // 경도(Longitude)
    val y: Double
)



