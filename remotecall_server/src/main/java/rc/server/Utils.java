package rc.server;

import org.wulfnoth.FileDecrypt;
import org.wulfnoth.rc.commons.Configuration;
import org.wulfnoth.rc.commons.Constant;

import java.io.File;
import java.io.IOException;

/**
 * @author Young
 */
public class Utils {

	private String keyPath = Configuration.getString("key.path");

	private String dataDir = Configuration.getString("path.data.dir");

	private FileDecrypt decrypter;

	private static Utils INS = new Utils();

	private Utils() {
		File data = new File(dataDir);
		if (!data.exists()) data.mkdirs();
		try {
			decrypter = new FileDecrypt(keyPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void decrypt(File file) throws IOException {
		String fileName = file.getName();
		if (fileName.length() > Constant.CRYPT_POSTFIX.length()) {
			fileName = fileName.substring(0, fileName.length() - Constant.CRYPT_POSTFIX.length());
		} else {
			throw new IOException("文件名错误");
		}
		INS.decrypter.decrypt(file.getAbsolutePath(),
				INS.dataDir + File.separator + fileName);
	}

}
