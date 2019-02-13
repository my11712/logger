package com.orhanobut.logger.expand;

import java.util.Comparator;

/**
 * 字符串比较
 */

public class CompratorByLastModified implements Comparator<String> {
    @Override
    public int compare(String lhs, String rhs) {
        int result = lhs.compareTo(rhs);
        if (result > 0) {
            return 1;
        } else if (result < 0) {
            return -1;
        } else {
            return 0;
        }
    }
}
