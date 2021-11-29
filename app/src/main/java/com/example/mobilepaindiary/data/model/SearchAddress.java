package com.example.mobilepaindiary.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @Author: Moon-Ete
 * @CreateDate: 2021/5/14 22:42
 * [
 *     {
 *         "place_id":"4215867",
 *         "licence":"Data © OpenStreetMap contributors, ODbL 1.0. https:\/\/www.openstreetmap.org\/copyright",
 *         "osm_type":"node",
 *         "osm_id":"452356972",
 *         "boundingbox":[
 *             "14.5635975",
 *             "14.5636975",
 *             "121.0288986",
 *             "121.0289986"
 *         ],
 *         "lat":"14.5636475",
 *         "lon":"121.0289486",
 *         "display_name":"Tiananmen, Makati Avenue, Bel-Air, Makati, District I, Makati, Metro Manila, 1200, 菲律賓",
 *         "class":"amenity",
 *         "type":"restaurant",
 *         "importance":0.101,
 *         "icon":"http:\/\/ip-10-116-128-153.mq-us-west-2.ec2.aolcloud.net\/nominatim\/images\/mapicons\/food_restaurant.p.20.png"
 *     },
 *     {
 *         "place_id":"76650653",
 *         "licence":"Data © OpenStreetMap contributors, ODbL 1.0. https:\/\/www.openstreetmap.org\/copyright",
 *         "osm_type":"way",
 *         "osm_id":"25097203",
 *         "boundingbox":[
 *             "39.9072282",
 *             "39.9075301",
 *             "116.3906498",
 *             "116.3918383"
 *         ],
 *         "lat":"39.9073285",
 *         "lon":"116.391242416486",
 *         "display_name":"天安门, 东长安街, 崇文, 北京市, 东城区, 北京市, 100010, 中国",
 *         "class":"building",
 *         "type":"yes",
 *         "importance":0.001
 *     }
 * ]
 */
public class SearchAddress {
    @SerializedName("place_id")
    private String placeId;
    @SerializedName("licence")
    private String licence;
    @SerializedName("osm_type")
    private String osmType;
    @SerializedName("osm_id")
    private String osmId;
    @SerializedName("boundingbox")
    private List<String> boundingbox;
    @SerializedName("lat")
    private String lat;
    @SerializedName("lon")
    private String lon;
    @SerializedName("display_name")
    private String displayName;
    @SerializedName("class")
    private String classX;
    @SerializedName("type")
    private String type;
    @SerializedName("importance")
    private Double importance;
    @SerializedName("icon")
    private String icon;

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public String getOsmType() {
        return osmType;
    }

    public void setOsmType(String osmType) {
        this.osmType = osmType;
    }

    public String getOsmId() {
        return osmId;
    }

    public void setOsmId(String osmId) {
        this.osmId = osmId;
    }

    public List<String> getBoundingbox() {
        return boundingbox;
    }

    public void setBoundingbox(List<String> boundingbox) {
        this.boundingbox = boundingbox;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getClassX() {
        return classX;
    }

    public void setClassX(String classX) {
        this.classX = classX;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getImportance() {
        return importance;
    }

    public void setImportance(Double importance) {
        this.importance = importance;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
