package one.show.live.common.cache;

import java.sql.SQLException;

import one.show.live.common.persistent.DbManager;
import one.show.live.common.po.POMember;

/**
 * Created by clarkM1ss1on on 2018/5/16
 */
public enum MemberCacheManager {
    INSTANCE;

    public POMember getCachedById(String uid) {
        POMember data = null;
        try {
            data = DbManager
                    .INSTANCE
                    .getDao(POMember.class)
                    .queryForId(uid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public void saveAsCache(POMember poMember) {
        try {
            DbManager.INSTANCE
                    .getDao(POMember.class)
                    .createOrUpdate(poMember);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
