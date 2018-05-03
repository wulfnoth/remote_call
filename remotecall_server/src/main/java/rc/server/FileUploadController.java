package rc.server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import org.wulfnoth.rc.commons.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

/**
 * @author Young
 */
@Controller
public class FileUploadController {

	private String tempPath = Configuration.getString("path.data.temp");

	private File tempDir = null;

	private synchronized void init() {
		if (tempDir == null) {
			tempDir = new File(tempPath);
		}
		if (!tempDir.exists())
			tempDir.mkdirs();
	}

	@RequestMapping("/postFile")
	public void postFile(HttpServletRequest request, HttpServletResponse response) {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession()
				.getServletContext());

		if (multipartResolver.isMultipart(request)) {

			StandardMultipartHttpServletRequest multiRequest = (StandardMultipartHttpServletRequest) request;
			Iterator<String> iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				MultipartFile multipartFile = multiRequest.getFile(iter.next());
				if (multipartFile != null) {
					String fileName = multipartFile.getOriginalFilename();
					init();
					if (fileName.trim().length() > 0) {
						File file = new File(tempPath + File.separator + fileName);
						try (FileOutputStream outputStream =
								     new FileOutputStream(file)) {
							outputStream.write(multipartFile.getBytes());
							Utils.decrypt(file);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

}
