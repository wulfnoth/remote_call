package org.wulfnoth.rc.commons;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Young
 */
public class ProcessMessage implements Serializable {

	private String filename = null;

	private List<String> output = new ArrayList<>();

	private List<String> err = new ArrayList<>();

	public ProcessMessage(String filename) {

	}

	public ProcessMessage() {

	}

	public void addMsg(String msg) {
		output.add(msg);
	}

	public void addErr(String msg) {
		err.add(msg);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("out");
		output.forEach(s -> sb.append("\n\t").append(s));
		sb.append("\n").append("err");
		err.forEach(s -> sb.append("\n\t").append(s));
		return sb.toString();
	}

	public String toJsonString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	public static ProcessMessage fromJsonString(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, ProcessMessage.class);
	}

	public static void main(String[] args) {
		String str = "{\"output\":[\"a\",\"b\"],\"err\":[]}";
		System.out.println(fromJsonString(str));
//		ProcessMessage processMessage = new ProcessMessage();
//		processMessage.addMsg("a");
//		processMessage.addMsg("b");
//		System.out.println(toJsonString(processMessage));
	}


}
