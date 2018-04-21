package one.show.live.media.presenter;

import one.show.live.media.model.UpShareRequest;

public class UpSharePresenter {

   public void upShare(String name,String vid){

       new UpShareRequest() {
           @Override
           public void onFinish(boolean isSuccess, String msg, Object data) {

           }
       }.setData(name,vid);
   }
}