package com.liminghuang.demo;

/**
 * Description:
 *
 * @author <a href="mailto:huanglm@guahao.com">Adaministrator</a>
 * @version 2.6.0
 * @since 2.6.0
 */
public class TestMessage {
    private int seq;

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    @Override
    public String toString() {
        return "TestMessage{" +
                "seq=" + seq +
                '}';
    }
}
