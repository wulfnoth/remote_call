package org.wulfnoth.rc.commons;

import java.io.IOException;
import java.util.*;

/**
 * @author Young
 */
public class ProcessUtils {

	public static final String HEAD = "HEAD";

	public static final String LINECOUNT = "LINECOUNT";

	public static final String COMPRESS = "COMPRESS";

	private static String customENV = "";
	static {
		StringJoiner sj = new StringJoiner(":");
		Configuration.getStrings("env").forEach(sj::add);
		customENV = sj.toString();
	}

	public static ProcessMessage runPython(String file, String... args) throws IOException {
		List<String> argsList = new ArrayList<>();
		argsList.add("python");
		argsList.add(file);
		for (String arg : args) {
			argsList.add("\"" + arg.replaceAll("\"", "\\\\\"") + "\"");
		}
		StringJoiner sj = new StringJoiner(" ");
		argsList.forEach(sj::add);
		System.out.println(sj.toString());

		return runBackground(sj.toString());
	}

	public static ProcessMessage runShellCMD(String c, String file) throws IOException {
		String cmd = null;
		switch (c) {
			case HEAD:
				cmd = "head " + file;
				break;
			case LINECOUNT:
				cmd = "wc -l " + file;
				break;
			case COMPRESS:
				cmd = "7za a " + file + ".7z " + file;
				break;
			default:
				throw new UnsupportedOperationException("command \"" + c + "\" unsupported");
		}
		ProcessMessage processMessage = new ProcessMessage();
		run(cmd, processMessage);
		return processMessage;
	}

	private static ProcessMessage runBackground(String cmd) throws IOException {

		String tempFile = System.currentTimeMillis() + ".out";
		cmd = String.format("nohup %s > %s 2>&1 &", cmd, tempFile);
		ProcessMessage processMessage = new ProcessMessage(tempFile);
		run(cmd, processMessage);

		return processMessage;
	}

	private static void run(String cmd, ProcessMessage msg) throws IOException {

		// 构建ProcessBuilder

		ProcessBuilder builder = new ProcessBuilder("/bin/sh", "-c", cmd);
		Map<String, String> env = builder.environment();
		env.put("PATH", customENV + ":" + env.get("PATH"));

		Process start = builder.start();
		Scanner scanner = new Scanner(start.getInputStream());
		Scanner errorScanner = new Scanner(start.getErrorStream());

        while (scanner.hasNextLine()) {
        	msg.addMsg(scanner.nextLine());
        }
		while (errorScanner.hasNextLine()) {
			msg.addErr(errorScanner.nextLine());
		}
		scanner.close();
		errorScanner.close();
	}

	public static void main(String[] args) throws IOException {
	}

}
