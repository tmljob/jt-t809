package io.tml.iov.inferior.client.executor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatCount {

    private int sucessCnt = 0;
    private int failCnt = 0;

    public void judeResult(boolean sendResult) {
        if (sendResult) {
            sucessCnt++;
        } else {
            failCnt++;
        }
    }
}
