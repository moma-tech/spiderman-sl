package net.kernal.spiderman.word;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class HtmlToWord {
	public static void main(String[] args) {
		HtmlToWord h2w = new HtmlToWord();
		try {
			h2w.htmlToWord2();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void htmlToWord2() throws Exception {
		List<String> fNames = this.getFileNames();
		String cat = "flfg";
		for (String a : fNames) {
			String fileName = a;

			InputStream bodyIs = new FileInputStream("store\\result\\" +cat+"\\"+ fileName);
			// InputStream cssIs = new FileInputStream("");
			String body = this.getContent(bodyIs);
			// String css = this.getContent(cssIs);
			// 拼一个标准的HTML格式文档
			String content = "<html><head></head><body>" + body + "</body></html>";
			InputStream is = new ByteArrayInputStream(content.getBytes("GBK"));
			OutputStream os = new FileOutputStream("store\\word\\" +cat+"\\" + fileName + ".doc");
			this.inputStreamToWord(is, os);
		}
	}

	public List<String> getFileNames() {
		List<String> fNames = new ArrayList<String>();
		String path = "store\\result\\";
		File f = new File(path);
		if (f.isDirectory()) {
			File[] fs = f.listFiles(new FileFilter() {
				public boolean accept(File file) {
					if (file.isFile()) {
						return true;
					} else {
						return false;
					}
				}
			});

			for (File file : fs) {
				fNames.add(file.getName());
			}
		}
		return fNames;
	}

	/**
	 * 把is写入到对应的word输出流os中 不考虑异常的捕获，直接抛出
	 * 
	 * @param is
	 * @param os
	 * @throws IOException
	 */
	private void inputStreamToWord(InputStream is, OutputStream os) throws IOException {
		POIFSFileSystem fs = new POIFSFileSystem();
		// 对应于org.apache.poi.hdf.extractor.WordDocument
		fs.createDocument(is, "WordDocument");
		fs.writeFilesystem(os);
		fs.close();
		os.close();
		is.close();
	}

	/**
	 * 把输入流里面的内容以UTF-8编码当文本取出。 不考虑异常，直接抛出
	 * 
	 * @param ises
	 * @return
	 * @throws IOException
	 */
	private String getContent(InputStream... ises) throws IOException {
		if (ises != null) {
			StringBuilder result = new StringBuilder();
			BufferedReader br;
			String line;
			for (InputStream is : ises) {
				br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				while ((line = br.readLine()) != null) {
					result.append(line);
				}
			}
			return result.toString();
		}
		return null;
	}
}
