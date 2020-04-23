package co.id.gmedia.fiberstream.utils;

import android.net.Uri;

/**
 * Created by Shin on 02/08/2017.
 */

public class ServerURL {
    private static String base_url ="http://gmedia.bz/zigystreaming/api/";

    public static String get_kategori = base_url+"master/ms_kategori";
    public static String get_konten_streaming = base_url+"master/konten_streaming";
    public static String get_slider =base_url+"master/iklan";
    public static String get_menu = base_url+"authentication/menu";
    public static String post_fcmid = base_url+"authentication/auth";
    public static String get_kategori_streaming = base_url+"streaming/kategori_streaming";
//    public static String get_konten_streaming = base_url+"streaming/item_streaming";
    public static String get_logo = base_url+"master/logo_apps";
    public static String get_channel = base_url+"live/list_live_streaming";
    public static String get_kategori_channel = base_url+"live/kategori_channel";
    public static String get_id_kategori_first = base_url+"live/id_kategori";
    public static String get_timertv = base_url+"master/timer_tv";
    public static String get_advertisement = base_url+"master/advertisement";
    public static String get_appear_text = base_url+"master/appear_text";

    public static final  String getKategori= base_url+"api/item/kategori_item/";
    public static final String url_profile_device = base_url+"authentication/profile_device";
    public static final  String getItemTV= base_url+"api/item/item_tv/";
    public static final  String base_url_fcm= "https://fcm.googleapis.com/fcm/send";

    public static String baseUrl = "http://gmediatv.gmedia.bz/";
//    public static String baseUrl = "http://192.168.20.72/project/gmedia_tv/";

    public static String getLink = baseUrl + "api/link/get_link/";
    //public static String getLink = baseUrl + "api/link/get_link_dummy/";
    public static String getTimerTV = baseUrl + "api/link/get_timer_tv/";
    public static String getAds = baseUrl + "api/link/get_ads/";
    public static String getAppearText = baseUrl + "api/link/get_appear_text/";
    public static String getLogo = baseUrl + "api/link/get_logo/";
    public static String testConnection = baseUrl + "api/link/test_connection/";

}
