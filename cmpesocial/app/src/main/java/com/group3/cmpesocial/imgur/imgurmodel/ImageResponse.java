package com.group3.cmpesocial.imgur.imgurmodel;

/**
 * Created by AKiniyalocts on 1/14/15.
 *
 * Response from imgur when uploading to the server.
 */
public class ImageResponse {
  public boolean success;
  public int status;
  public UploadedImage data;

  public static class UploadedImage{

    public String link;

    @Override public String toString() {
      return "UploadedImage{" +
          "link='" + link + '\'' +
          '}';
    }
  }

  @Override public String toString() {
    return "ImageResponse{" +
        "success=" + success +
        ", status=" + status +
        ", data=" + data.toString() +
        '}';
  }
}
