package com.liminghuang.demo;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Description:
 *
 * @author <a href="mailto:1569642270@qq.com">Adaministrator</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public interface Api {
    /**
     * @param userId 用户id
     * @return
     */
    @GET(value = "base/usercontact/{userid}")
    List<String> getList(@Path(value = "userid") String userId);
}
