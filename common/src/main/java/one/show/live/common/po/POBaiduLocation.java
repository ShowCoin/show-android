package one.show.live.common.po;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;

import java.util.List;

import one.show.live.common.ui.BaseApplication;

public class POBaiduLocation {

    public LocationClient mLocationClient;
    public  static String address;
    public  static double latitude, longitude;
    public  static String country;

    public static boolean isOk;//定位是否成功

    public void initLocation(Context context) {

        mLocationClient = new LocationClient(context);     //声明LocationClient类
        mLocationClient.registerLocationListener(new MyLocationListener());    //注册监听函数

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    public class MyLocationListener implements BDLocationListener {



        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());


            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            Log.i("BaiduLocationApiDem", sb.toString());
            if (location == null) {
                address = "";
                return;
            }


            if (location.getLocType() != BDLocation.TypeServerError &&
                    location.getLocType() != BDLocation.TypeNetWorkException &&
                    location.getLocType() != BDLocation.TypeCriteriaException) {
                isOk = true;

//                address = location.getCity();
//                latitude = location.getLatitude();//获取纬度
//                longitude = location.getLongitude();//获取经度
//                country = location.getCountry();//国家信息

                 address = location.getCity();
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                country = location.getCountry();//国家信息

                SharedPreferences preferences= BaseApplication.getContext().getSharedPreferences("BaiduLocation",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("baiduAddress",location.getCity());
                editor.putString("baiduLatitude",String.valueOf(location.getLatitude()));
                editor.putString("baiduLongitude",String.valueOf(location.getLongitude()));
                editor.putString("baiduCountry",location.getCountry());
                editor.commit();

                mLocationClient.stop();
            }

        }
    }

    public static boolean isLocationInfoSuccess(){
        if(latitude != 0d && longitude != 0d){
            return true;
        }
        return false;
    }


    public static void checkLocationInfo(){
        try {
            if(latitude == 0d || longitude == 0d){
                SharedPreferences preferences = BaseApplication.getContext().getSharedPreferences("BaiduLocation", Context.MODE_PRIVATE);
                address =preferences.getString("baiduAddress","");
                latitude =Double.parseDouble(preferences.getString("baiduLatitude",""));
                longitude =Double.parseDouble(preferences.getString("baiduLongitude",""));
                country =preferences.getString("baiduCountry","");
            }

        }catch (Exception e){

        }

    }
}
