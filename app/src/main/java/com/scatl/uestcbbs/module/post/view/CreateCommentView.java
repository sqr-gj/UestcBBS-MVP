package com.scatl.uestcbbs.module.post.view;

import com.scatl.uestcbbs.entity.AttachmentBean;
import com.scatl.uestcbbs.entity.SendPostBean;
import com.scatl.uestcbbs.entity.UploadResultBean;

import java.io.File;
import java.util.List;

/**
 * author: sca_tl
 * description:
 * date: 2020/1/25 13:04
 */
public interface CreateCommentView {
    void onSendCommentSuccess(SendPostBean sendPostBean, List<String> imgUrls);
    void onSendCommentError(String msg);
    void onUploadSuccess(UploadResultBean uploadResultBean);
    void onUploadError(String msg);
    void onCompressImageSuccess(List<File> compressedFiles);
    void onCompressImageFail(String msg);
    void onPermissionGranted(int action);
    void onPermissionRefused();
    void onPermissionRefusedWithNoMoreRequest();
    void onExit();
    void onStartUploadAttachment();
    void onUploadAttachmentSuccess(AttachmentBean attachmentBean, String msg);
    void onUploadAttachmentError(String msg);
}
