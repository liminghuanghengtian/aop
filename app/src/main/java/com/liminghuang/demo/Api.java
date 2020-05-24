package com.liminghuang.demo;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Description:
 *
 * @author <a href="mailto:huanglm@guahao.com">Adaministrator</a>
 * @version 2.6.0
 * @since 2.6.0
 */
public interface Api {
    /**
     * @param userId 用户id
     * @return
     */
    @GET(value = "base/usercontact/{userid}")
    List<String> getList(@Path(value = "userid") String userId);
}
