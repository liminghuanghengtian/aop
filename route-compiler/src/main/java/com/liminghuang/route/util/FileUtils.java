package com.liminghuang.route.util;

import java.io.File;

/**
 * Description:
 *
 * @author <a href="mailto:huanglm@guahao.com">Adaministrator</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public class FileUtils {

    /**
     * 删除文件夹（强制删除）
     *
     * @param path
     */
    public static void deleteAllFilesOfDir(File path) {
        if (null != path) {
            if (!path.exists()) {
                return;
            }

            if (path.isFile()) {
                boolean result = path.delete();
                int tryCount = 0;
                while (!result && tryCount++ < 10) {
                    System.gc(); // 回收资源
                    result = path.delete();
                }
            }

            File[] files = path.listFiles();
            if (null != files) {
                for (File file : files) {
                    deleteAllFilesOfDir(file);
                }
            }

            // 目录不删除
            // path.delete();
        }
    }
}
