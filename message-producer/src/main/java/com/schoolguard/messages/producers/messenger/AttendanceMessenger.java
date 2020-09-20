package com.schoolguard.messages.producers.messenger;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.*;

/**
 * Created by Rogers on 15-5-4.
 */
public class AttendanceMessenger implements Serializable, Transformer{
    private static final long serialVersionUID = 898394679382L;

    private JobType jobType = JobType.ATTENDANCE;
    private int schoolId;
    private String schoolName;
    private int studentId;
    private String studentName;
    private String imageUrl;
    private String location;
    private long timestamp;

    public AttendanceMessenger(int schoolId, String schoolName, int studentId, String studentName,
                               String imageUrl, String location, long timestamp) {
        this.jobType = JobType.ATTENDANCE;
        this.schoolId = checkNotNull(schoolId);
        this.schoolName = checkNotNull(schoolName);
        this.studentId = checkNotNull(studentId);
        this.studentName = checkNotNull(studentName);
        this.imageUrl = checkNotNull(imageUrl);
        this.location = checkNotNull(location);
        this.timestamp = checkNotNull(timestamp);
    }

    public JobType getJobType() {
        return jobType;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


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
    public static AttendanceMessenger fromBytes(byte[] bytes){
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            return (AttendanceMessenger) in.readObject();
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
