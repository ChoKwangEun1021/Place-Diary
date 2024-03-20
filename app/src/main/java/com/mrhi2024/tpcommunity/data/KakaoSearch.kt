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

