package com.test;

import com.bean.PolicyBO;
import com.bean.diff.DiffVO;
import com.thoughtworks.xstream.XStream;
import com.util.JxpathCommonUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class TestMain {

	private static Logger log = LogManager.getLogger(TestMain.class.getName());

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		TestMain tm = new TestMain();

		JxpathCommonUtil jxpathCommon = new JxpathCommonUtil();
		jxpathCommon.setProperties("config/key.properties");
		PolicyBO o1 = tm.buildPolicyBO("config/policy1.xml");
		PolicyBO o2 = tm.buildPolicyBO("config/policy3.xml");
		
		long time = System.currentTimeMillis();
		List<DiffVO> lst = jxpathCommon.compareBean(o1, o2);
		for (DiffVO diff : lst) {
			log.error(diff.toString());
		}
		log.error(System.currentTimeMillis() - time);

		System.exit(0);

	}

	public PolicyBO buildPolicyBO(String fileName) {
		File file = new File(fileName);
		BufferedReader reader = null;
		String s = "";
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
				s += tempString;
				line++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		XStream xs = new XStream();

		return (PolicyBO) xs.fromXML(s);
	}


}
