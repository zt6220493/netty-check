package com.schoolguard.messages.producers.messenger;

import java.util.Date;
import java.util.Set;
import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Created by Rogers on 15-4-30.
 */
public class NoticeMessenger extends Messenger {
    private ReceiverType receiverType;
    private String content;
    private String mediaUrl;
    private MediaType mediaType;

    public NoticeMessenger(long messageId, int schoolId, String schoolName, int senderId, String senderName, String senderAvatar, Set<Integer> receiverIds, boolean sendToSelf, String signature, Date scheduleTime, Date createTime, ReceiverType receiverType, String content, String mediaUrl, MediaType mediaType) {
        super(JobType.NOTICE, messageId, schoolId, schoolName, senderId, senderName, senderAvatar, receiverIds, sendToSelf, signature, scheduleTime, createTime);
        this.receiverType = receiverType;
        this.content = content;
        this.mediaUrl = mediaUrl;
        this.mediaType = mediaType;
    }

    public NoticeMessenger(long messageId,
                           int schoolId,
                           String schoolName,
                           int senderId,
                           String senderName,
                           String senderAvatar,
                           Set<Integer> receiverIds,
                           ReceiverType receiverType,
                           boolean sendToSelf,
                           String signature,
                           Date scheduleTime,
                           Date createTime,
                           String content) {
        super(JobType.NOTICE, messageId, schoolId, schoolName, senderId, senderName, senderAvatar,
                receiverIds, sendToSelf, signature, scheduleTime, createTime);
        this.content = checkNotNull(content);
        this.receiverType = checkNotNull(receiverType);
    }

    public NoticeMessenger(long messageId,
                           int schoolId,
                           String schoolName,
                           int senderId,
                           String senderName,
                           String senderAvatar,
                           Set<Integer> receiverIds,
                           ReceiverType receiverType,
                           boolean sendToSelf,
                           String signature,
                           Date scheduleTime,
                           Date createTime,
                           String mediaUrl,
                           MediaType mediaType) {
        super(JobType.NOTICE, messageId, schoolId, schoolName, senderId, senderName, senderAvatar,
                receiverIds, sendToSelf, signature, scheduleTime, createTime);
        this.mediaType = checkNotNull(mediaType);
        this.mediaUrl = checkNotNull(mediaUrl);
        this.receiverType = checkNotNull(receiverType);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public ReceiverType getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(ReceiverType receiverType) {
        this.receiverType = receiverType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }
}
