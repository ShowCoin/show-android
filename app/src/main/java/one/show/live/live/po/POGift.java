package one.show.live.live.po;

import com.google.gson.annotations.Expose;
import java.io.Serializable;

public class POGift implements Serializable{


//        "id": 2,
//                "exp": 10,
//                "icon": "http://img.meelive.cn/NTg4MzMxNDUxODkzMDkw.jpg",
//                "price": 1.0,
//                "name": "樱花雨",
//                "image": "http://img.meelive.cn/OTc4MzYxNDUxODkzMDk4.jpg",
//                "type": 1

    private int id;
    private int exp;
    private String icon;
    private int price;
    private String name;
    private String image;
    private int type;

    //缺失字段
    public String sourceUrl;


    public boolean isCheck;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

}
