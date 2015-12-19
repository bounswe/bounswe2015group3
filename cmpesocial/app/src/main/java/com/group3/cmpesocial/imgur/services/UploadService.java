package com.group3.cmpesocial.imgur.services;

import android.content.Context;
import android.util.Log;

import com.group3.cmpesocial.R;
import com.group3.cmpesocial.imgur.imgurmodel.ImageResponse;
import com.group3.cmpesocial.imgur.imgurmodel.ImgurAPI;
import com.group3.cmpesocial.imgur.utils.NetworkUtils;

import java.io.File;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

/**
 * Created by AKiniyalocts on 1/12/15.
 * <p/>
 * Our upload service. This creates our restadapter, uploads our image, and notifies us of the response.
 */
public class UploadService {
    public final static String TAG = UploadService.class.getSimpleName();

    private Context mContext;

    public UploadService(Context context) {
        this.mContext = context;
    }

    public void Execute(File image, Callback<ImageResponse> callback) {

        final Callback<ImageResponse> cb = callback;

        if (!NetworkUtils.isConnected(mContext)) {
            //Callback will be called, so we prevent a unnecessary notification
            cb.failure(null);
            return;
        }

        RestAdapter restAdapter = buildRestAdapter();

        restAdapter.create(ImgurAPI.class).postImage(mContext.getString(R.string.imgurClientId), null, null, null, null,
                new TypedFile("image/*", image),
                new Callback<ImageResponse>() {
                    @Override
                    public void success(ImageResponse imageResponse, Response response) {
                        if (cb != null) cb.success(imageResponse, response);
                        if (response == null) {
                            /*
                             Notify image was NOT uploaded successfully
                            */
                            Log.d(TAG, "upload error");
                            return;
                        }
                        /*
                        Notify image was uploaded successfully
                        */
                        if (imageResponse.success) {
                            Log.d(TAG, "image uploaded");
                            Log.d(TAG, imageResponse.data.link);
                            Log.d(TAG, imageResponse.toString());
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (cb != null) cb.failure(error);
                        Log.d(TAG, "retrofit error");
                    }
                });
    }

    private RestAdapter buildRestAdapter() {
        RestAdapter imgurAdapter = new RestAdapter.Builder().setEndpoint(ImgurAPI.server).build();

        /*
        Set rest adapter logging if we're already logging
        */
        imgurAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        return imgurAdapter;
    }
}
