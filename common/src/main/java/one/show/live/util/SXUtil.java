package one.show.live.util;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class SXUtil {
    final public static int SXDEVICE_NETWROK_TYPE_UNKONW = 1;
    final public static int SXDEVICE_NETWROK_TYPE_GRPS = 2;
    final public static int SXDEVICE_NETWROK_TYPE_3G = 3;
    final public static int SXDEVICE_NETWROK_TYPE_4G = 4;
    final public static int SXDEVICE_NETWROK_TYPE_WIFI = 5;

    public static int getNetworkType(Application app){
        if(app==null){
            return SXDEVICE_NETWROK_TYPE_UNKONW;
        }

        try{
            ConnectivityManager connManager = (ConnectivityManager) app.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo currentNetwork = connManager.getActiveNetworkInfo();
            if(currentNetwork!=null && currentNetwork.isAvailable()){
                if(currentNetwork.isConnected()){
                    if(currentNetwork.getType()==ConnectivityManager.TYPE_WIFI){
                        return SXDEVICE_NETWROK_TYPE_WIFI;
                    }else if(currentNetwork.getType()==ConnectivityManager.TYPE_MOBILE){
                        switch (currentNetwork.getSubtype()){
                            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                                return SXDEVICE_NETWROK_TYPE_UNKONW;
                            case TelephonyManager.NETWORK_TYPE_GPRS:
                            case TelephonyManager.NETWORK_TYPE_EDGE:
                            case TelephonyManager.NETWORK_TYPE_CDMA:
                            case TelephonyManager.NETWORK_TYPE_1xRTT:
                            case TelephonyManager.NETWORK_TYPE_IDEN:
                                return SXDEVICE_NETWROK_TYPE_GRPS;
                            case TelephonyManager.NETWORK_TYPE_UMTS:
                            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                            case TelephonyManager.NETWORK_TYPE_HSDPA:
                            case TelephonyManager.NETWORK_TYPE_HSUPA:
                            case TelephonyManager.NETWORK_TYPE_HSPA:
                            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                            case TelephonyManager.NETWORK_TYPE_EHRPD:
                            case TelephonyManager.NETWORK_TYPE_HSPAP:
                                return SXDEVICE_NETWROK_TYPE_3G;
                            case TelephonyManager.NETWORK_TYPE_LTE:
                                return SXDEVICE_NETWROK_TYPE_4G;
                            default:
                                return SXDEVICE_NETWROK_TYPE_UNKONW;
                        }
                    }
                }
            }
        } catch (Exception ignored) {

        }
        return SXDEVICE_NETWROK_TYPE_UNKONW;
    }


}
