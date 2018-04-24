package one.show.live.media.po;

/**
 * Created by Administrator on 2016/7/21 0021.
 */
public class POPublisherEnd {

  private int liked;
  private int viewed;
  private int receive;

  public int getLiked() {
    return liked;
  }

  public void setLiked(int liked) {
    this.liked = liked;
  }

  public int getViewed() {
    return viewed;
  }

  public void setViewed(int viewed) {
    this.viewed = viewed;
  }

  public int getReceive() {
    return receive;
  }

  public void setReceive(int receive) {
    this.receive = receive;
  }

  public POIMEnd convertToPOIMEnd(){
    return new POIMEnd(2,viewed,liked,receive);
  }
}
