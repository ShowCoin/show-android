package one.show.live.live.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

public class AnimBigFrameConfigs {

    public static final int Yellow_Car = 1;//黄色车
    public static final int Red_Car = 2;//红色车
    public static final int Crystal_Shoes = 3;//水晶鞋
    public static final int Bao_Bao = 4;//包包
    public static final int Diamond = 5;//钻石
    public static final int Yacht = 6;//游艇
    public static final int Perfume = 7;//香水
    public static final int Petal = 8;//花瓣

    private SoftReference<AnimationDrawable> softFrameAnim;
    private Context mContext;
    private Resources res;

    public AnimBigFrameConfigs(Context context) {
        this.mContext = context;
        res = mContext.getResources();
    }

    public AnimationDrawable loadAnimationDrawable(int gifType) {

        int gifNumber = 0;
        String gifName = null;

        switch (gifType){
            case Yellow_Car:
                gifNumber = 54;
                gifName = "huangsepaoche_00";
                break;

            case Red_Car:
                gifNumber = 60;
                gifName = "hongsepaoche_00000";
                break;

            case Crystal_Shoes:
                gifNumber = 61;
                gifName = "shuijingxie_00";
                break;

            case Bao_Bao:
                gifNumber = 60;
                gifName = "baobao_00";
                break;

            case Diamond:
                gifNumber = 72;
                gifName = "zuanjie_00000";
                break;

            case Yacht:
                gifNumber = 65;
                gifName = "youting_00";
                break;

            case Perfume:
                gifNumber = 40;
                gifName = "xiangshui_00000";
                break;

            case Petal:
                gifNumber = 56;
                gifName = "huabanyu_00";
                break;
        }

        if (gifNumber == 0 || gifName == null){
            throw new RuntimeException("gifType Cannot be null");
        }

        if (softFrameAnim != null) {
            softFrameAnim.clear();
            softFrameAnim = null;
        }
        softFrameAnim = new SoftReference<AnimationDrawable>(new AnimationDrawable());
        AnimationDrawable frameAnim = softFrameAnim.get();
            for (int i = 0; i < gifNumber; i++) {
                Drawable drawable = ContextCompat.getDrawable(mContext,getByNameResource(getName(gifName, i)));
                WeakReference<Drawable> weakReference = new WeakReference<Drawable>(drawable);
                drawable = null;
                frameAnim.addFrame(weakReference.get(), 150);
            }
        frameAnim.setOneShot(true);


        return frameAnim;
    }

    private String getName(String n,int num){
        if (num < 10){
            n = n.substring(0,n.length()-1)+num;
        }else{
            n = n.substring(0,n.length()-2)+num;
        }
        return n;
    }

    private int getByNameResource(String imageName) {
        int resId = res.getIdentifier(imageName, "drawable", mContext.getPackageName());
        return resId;
    }
}