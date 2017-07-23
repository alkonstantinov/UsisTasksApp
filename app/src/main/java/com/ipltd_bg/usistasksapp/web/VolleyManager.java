package com.ipltd_bg.usistasksapp.web;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class VolleyManager {
	private static final String TAG = "VolleyManager";
	private static VolleyManager mInstance;
	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;
	private static Context mCtx;

	private VolleyManager(Context ctx) {
		mCtx = ctx;
		try {
			mRequestQueue = getRequestQueue();
		} catch (Exception e) {
			Log.e(TAG, "", e);
		}

		mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
			private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(20);

			@Override
			public Bitmap getBitmap(String url) {
				return cache.get(url);
			}

			@Override
			public void putBitmap(String url, Bitmap bitmap) {
				cache.put(url, bitmap);
			}
		});
	}

	public static synchronized VolleyManager getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new VolleyManager(context);
		}
		return mInstance;
	}

	public RequestQueue getRequestQueue() throws Exception {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
		}
		return mRequestQueue;
	}

	public void renewQueue() {
		Log.d(TAG, "renewQueue");
		mRequestQueue = null;
		try {
			getRequestQueue();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public <T> void addToRequestQueue(Request<T> req) {
		try {
			getRequestQueue().add(req);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ImageLoader getImageLoader() {
		return mImageLoader;
	}

}
