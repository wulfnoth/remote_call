package org.wulfnoth.rc.client.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.wulfnoth.FileEncrypt;
import org.wulfnoth.rc.commons.Configuration;
import org.wulfnoth.rc.commons.Constant;

import java.io.File;
import java.io.IOException;

/**
 * @author Young
 */
public class FileUtils {


	private static String uploadUrl = Configuration.getString("upload.url");
	private FileEncrypt encrypter;

	private static FileUtils INS = new FileUtils();

	private FileUtils() {
		try {
			encrypter = new FileEncrypt(Configuration.getString("key.path"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将文件提交至文件服务器
	 * @param file 文件对象
	 * @return FileStatus 上传结果
	 */
	public static String postFile(File file) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String result = null;
		try {
			HttpPost httpPost = new HttpPost(uploadUrl);
			MultipartEntityBuilder mEntityBuilder = MultipartEntityBuilder.create();
			mEntityBuilder.addBinaryBody("file", file);
			httpPost.setEntity(mEntityBuilder.build());
			response = httpclient.execute(httpPost);

			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				HttpEntity resEntity = response.getEntity();
				result = EntityUtils.toString(resEntity);
				// 消耗掉response
				EntityUtils.consume(resEntity);
			}
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		} finally {
			HttpClientUtils.closeQuietly(httpclient);
			HttpClientUtils.closeQuietly(response);
		}
		return result;
	}

	public static File encrypt(File file) {
		try {
			return INS.encrypter.encrypt(file.getAbsolutePath(),
					file.getAbsolutePath() + Constant.CRYPT_POSTFIX);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
//		File file = new File("");
//		System.out.println(file.getAbsolutePath());
		File file = encrypt(new File("D:\\Apache_Kafka.pdf"));
		System.out.println(file.getName());
		postFile(file);
	}

}
