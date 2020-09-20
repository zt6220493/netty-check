package com.schoolguard.messages.producers.messenger;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.*;
import java.util.Date;
import java.util.Set;


/**
 * Created by Rogers on 15-4-30.
 */
public abstract class Messenger implements Serializable, Transformer {
    protected static final long serialVersionUID = 898394893248L;

    //消息类型：考勤，通知，作业，成绩，OA短信
    protected JobType jobType;

    //各种消息的ID
    protected long messageId;
    protected int schoolId;
    protected String schoolName;
    protected int senderId;
    protected String senderName;
    protected String senderAvatar;
    protected Set<Integer> receiverIds; //对ExamMessenger不适用; 对assignment来说, 此ID为班级ID
    // 是否发送给自己
    protected boolean sendToSelf = false;
    protected String signature;
    protected Date scheduleTime;
    protected Date createTime;

    public Messenger(){}

    public Messenger(JobType jobType, long messageId, int schoolId, String schoolName,
                     int senderId, String senderName, String senderAvatar, Set<Integer> receiverIds,
                     boolean sendToSelf, String signature, Date scheduleTime, Date createTime) {
        this.jobType = checkNotNull(jobType);
        this.messageId = checkNotNull(messageId);
        this.schoolId = checkNotNull(schoolId);
        this.senderId = checkNotNull(senderId);
        this.schoolName = checkNotNull(schoolName);
        this.senderName = checkNotNull(senderName);
        this.senderAvatar = checkNotNull(senderAvatar);
        this.receiverIds = checkNotNull(receiverIds);
        this.sendToSelf = sendToSelf;
        this.signature = signature;
        this.scheduleTime = scheduleTime;
        this.createTime = checkNotNull(createTime);
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public int getSenderId() {
        return senderId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getSenderAvatar() {
        return senderAvatar;
    }

    public void setSenderAvatar(String senderAvatar) {
        this.senderAvatar = senderAvatar;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public Set<Integer> getReceiverIds() {
        return receiverIds;
    }

    public void setReceiverIds(Set<Integer> receiverIds) {
        this.receiverIds = receiverIds;
    }

    public boolean isSendToSelf() {
        return sendToSelf;
    }

    public void setSendToSelf(boolean sendToSelf) {
        this.sendToSelf = sendToSelf;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Date getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(Date scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 转换Messenger为byte array
     *
     * @return
     */
    @Override
    public byte[] toBytes(){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try{
            out = new ObjectOutputStream(bos);
            out.writeObject(this);
            return bos.toByteArray();
        } catch (IOException e){
            throw new RuntimeException(e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e){ }

            try{
                bos.close();
            } catch (IOException e){}
        }
    }

    /**
     * 转换字节码为java object, 需要再手动转换为需要的类型
     *
     * @param bytes
     * @return
     */
    public static Object fromBytes(byte[] bytes){
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            return in.readObject();
        } catch (ClassNotFoundException | IOException e){
            throw new RuntimeException(e);
        } finally {
            try {
                bis.close();
            } catch (IOException ex) {
                // ignore close exception
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
    }
}
