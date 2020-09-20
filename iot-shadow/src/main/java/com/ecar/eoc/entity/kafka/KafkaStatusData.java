package com.ecar.eoc.entity.kafka;

/**
 * 状态同步
 */
public class KafkaStatusData {
    // 设备号
    private String terminalId;

    // 消息Unix时间戳
    private long time;
    
    private KafkaStatusData() {

    }

    private static class Holder {
        private static KafkaStatusData instance = new KafkaStatusData();
    }

    public static KafkaStatusData getInstance() {
        return Holder.instance;
    }
    

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "KafkaStatusData{" +
                "terminalId='" + terminalId + '\'' +
                ", time=" + time +
                '}';
    }
}
