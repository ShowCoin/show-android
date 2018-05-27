//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package one.show.live.upload.model;

import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class VodInfo {
    private String title;
    private String desc;
    private Integer cateId;
    private List<String> tags;
    private String userData;
    private String coverUrl;
    private Boolean isProcess = Boolean.valueOf(true);
    private Boolean isShowWaterMark;
    private Integer priority;

    public VodInfo() {
    }

    public Boolean getIsProcess() {
        return this.isProcess;
    }

    public void setIsProcess(Boolean isProcess) {
        this.isProcess = isProcess;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<String> getTags() {
        return this.tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getUserData() {
        return this.userData;
    }

    public void setUserData(String userData) {
        this.userData = userData;
    }

    public String toVodJsonStringWithBase64() {
        JSONObject object = new JSONObject();

        try {
            JSONObject e = new JSONObject();
            e.put("Title", this.getTitle());
            e.put("Description", this.getDesc());
            e.put("CateId", String.valueOf(this.getCateId()));
            e.put("CoverUrl", this.getCoverUrl());
            e.put("IsProcess", this.getIsProcess().toString());
            String tags = "";
            if(this.getTags() != null && this.getTags().size() > 0) {
                tags = this.getTags().toString();
                tags = tags.substring(1, tags.length() - 1);
            }

            e.put("Tags", tags);
            if(null == this.isShowWaterMark && null == this.priority) {
                e.put("UserData", this.getUserData());
            } else {
                JSONObject userData = new JSONObject();
                if(null != this.isShowWaterMark && this.isShowWaterMark.booleanValue()) {
                    userData.put("IsShowWaterMark", "true");
                } else {
                    userData.put("IsShowWaterMark", "false");
                }

                userData.put("Priority", String.valueOf(this.getPriority()));
                e.put("UserData", userData);
            }

            object.put("Vod", e);
        } catch (JSONException var5) {
            var5.printStackTrace();
        }

        return Base64.encodeToString(object.toString().getBytes(), 2);
    }

    public String getCoverUrl() {
        return this.coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public Integer getCateId() {
        return this.cateId;
    }

    public void setCateId(Integer cateId) {
        this.cateId = cateId;
    }

    public Boolean getIsShowWaterMark() {
        return this.isShowWaterMark;
    }

    public void setIsShowWaterMark(Boolean isShowWaterMark) {
        this.isShowWaterMark = isShowWaterMark;
    }

    public Integer getPriority() {
        return this.priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}
