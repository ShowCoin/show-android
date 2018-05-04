package one.show.live.common.po;

import java.io.Serializable;

import one.show.live.common.util.StringUtils;

/**
 * Created by liuzehua on 2017/12/19.
 */

public class POPhotos implements Serializable{

    private int id;
    private String img;
    private String file;//若为视频文件，增加file
    public boolean isPlaceholder;
    public int status;//0：默认状态。1：为选中状态 2:选中状态
    public  String thumbnail;//缩略图

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getThumbnail() {
        return StringUtils.isNotEmpty(thumbnail)?thumbnail:img;
    }

    public POPhotos(){}
    public POPhotos(boolean isPlaceholder){
        this.isPlaceholder = isPlaceholder;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        POPhotos photo = (POPhotos) o;

        return id == photo.id;

    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
