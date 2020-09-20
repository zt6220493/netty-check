package com.schoolguard.messages.producers.messenger;

import java.util.Date;
import java.util.Set;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Rogers on 15-4-30.
 */
public class OAMessenger extends Messenger {
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public OAMessenger(long messageId,
                       int schoolId,
                       String schoolName,
                       int senderId,
                       String senderName,
                       String senderAvatar,
                       Set<Integer> receiverIds,
                       boolean sendToSelf,
                       String signature,
                       Date scheduleTime,
                       Date createTime,
                       String content) {
        super(JobType.OA_MSG, messageId, schoolId, schoolName, senderId, senderName, senderAvatar,
                receiverIds, sendToSelf, signature, scheduleTime, createTime);
        this.content = checkNotNull(content);
    }
}
