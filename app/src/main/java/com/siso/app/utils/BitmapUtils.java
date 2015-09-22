package com.siso.app.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;

/**
 * @author xiaqiang
 * @Describe Bitmap相关的操作类,例如压缩,流转换
 */
public class BitmapUtils {
	private static float targetWidth = 720f;
	private static float targetHeight = 1280f;
	private static long target = 100;// 目标大小为10kb
	private static int quality = 90;// (0~100)压缩质量

	public static Bitmap compressBmpFromFile(String srcPath) {
		return compressBmpFromFile(srcPath, quality, target, targetWidth,
				targetHeight);
	}

	public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
			double newHeight) {
		// 获取这个图片的宽和高
		float width = bgimage.getWidth();
		float height = bgimage.getHeight();
		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		// 计算宽高缩放率
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
				(int) height, matrix, true);
		return bitmap;
	}

	public static Bitmap compressImage(Bitmap image, int quality, long target) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, quality, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		while ((baos.toByteArray().length / 1024) > target) { // 循环判断如果压缩后图片是否大于target大小的kb,大于则继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, quality, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			if (quality >= 10) {
				quality -= 10;// 每次都减少10,若小于10%就按10%压缩
			}
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	public static Bitmap compressBmpFromBmp(Bitmap image, int quality,
			long target) {
		ByteArrayInputStream isBm = Bitmap2ByteArrayInputStream(image);
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		// newOpts.inPreferredConfig = Config.RGB_565;//
		// 降低图片从ARGB888到RGB565,图片配置系数用RGB_565,
		// 默认是24位的ARGB_888
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;// 这里设置高度为800f
		float ww = 480f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		// return compressImage(bitmap);// 原来的方法调用了这个方法企图进行二次压缩 ,其实是无效的
		return bitmap;
	}

	public static Bitmap compressBmpFromFile(String srcPath, int quality,
			long target, float targetWidth, float targetHeight) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = targetWidth;// 这里设置高度为800f
		float ww = targetWidth;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		// be = (int) ((w / STANDARD_WIDTH + h/ STANDARD_HEIGHT) / 2);
		// 结论二：图片比例压缩倍数 就是 （宽度压缩倍数+高度压缩倍数）/2
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return compressImage(bitmap, quality, target);// 压缩好比例大小后再进行质量压缩
	}

	public static File BitmapToFile(Bitmap bitmap, String dir, String fileName) {
		File dirFile = new File(dir);// 将要保存图片的路径
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		File file = new File(dirFile, fileName);
		try {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(file));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
			return file;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ByteArrayInputStream Bitmap2ByteArrayInputStream(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		return new ByteArrayInputStream(baos.toByteArray());
	}

	/**
	 * 根据网址获得图片，优先从本地获取，本地没有则从网络下载
	 */
	public static Bitmap getBitmap(String url, Context context) {
		String imageName = url
				.substring(url.lastIndexOf("/") + 1, url.length());
		File file = new File(getPath(context), imageName);
		if (file.exists()) {
			return BitmapFactory.decodeFile(file.getPath());
		}
		return getNetBitmap(url, file, context);
	}

	/**
	 * 根据传入的list中保存的图片网址，获取相应的图片列表
	 */
	public static List<Bitmap> getBitmap(List<String> list, Context context) {
		List<Bitmap> result = new ArrayList<Bitmap>();
		for (String strUrl : list) {
			Bitmap bitmap = getBitmap(strUrl, context);
			if (bitmap != null) {
				result.add(bitmap);
			}
		}
		return result;
	}

	/**
	 * 获取图片的存储目录，在有sd卡的情况下为 “/sdcard/apps_images/本应用包名/cach/images/”
	 * 没有sd的情况下为“/data/data/本应用包名/cach/images/”
	 */
	private static String getPath(Context context) {
		String path = null;
		boolean hasSDCard = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		String packageName = context.getPackageName() + "/cach/images/";
		if (hasSDCard) {
			path = "/sdcard/apps_images/" + packageName;
		} else {
			path = "/data/data/" + packageName;
		}
		File file = new File(path);
		boolean isExist = file.exists();
		if (!isExist) {

			file.mkdirs();

		}
		return file.getPath();
	}

	/**
	 * 网络可用状态下，下载图片并保存在本地
	 */
	private static Bitmap getNetBitmap(String strUrl, File file, Context context) {
		Bitmap bitmap = null;
		if (HttpUtils.isNetworkConnected(context)) {
			try {
				URL url = new URL(strUrl);
				HttpURLConnection con = (HttpURLConnection) url
						.openConnection();
				con.setDoInput(true);
				con.connect();
				InputStream in = con.getInputStream();
				bitmap = BitmapFactory.decodeStream(in);
				FileOutputStream out = new FileOutputStream(file.getPath());
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
				out.flush();
				out.close();
				in.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
			}
		}
		return bitmap;
	}

	/**
	 * 根据网址获得图片，优先从本地localPath获取，本地没有则从网络下载
	 */
	public static Bitmap getBitmap(String url, String localPath, Context context) {
		File file = new File(localPath);
		if (file.exists()) {
			return BitmapFactory.decodeFile(file.getPath());
		}
		if (url != null && !"".equals(url)) {
			return getNetBitmap(url, file, context);
		} else {
			return null;
		}
	}
	
	/**
	 * 
	 * @author Owater
	 * @createtime 2015-3-15 下午4:04:54
	 * @Decription 图片Uri转化成Bitmap
	 *
	 * @param context
	 * @param uri
	 * @return
	 */
	public static Bitmap decodeUriToBitmap(Context context, Uri uri) {
        if (context == null || uri == null) return null;

        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }
}
