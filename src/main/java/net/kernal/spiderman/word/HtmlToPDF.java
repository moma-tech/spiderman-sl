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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;

public class HtmlToPDF {

	public static void main(String[] args) {
		HtmlToPDF h2w = new HtmlToPDF();
		try {
			h2w.htmlToPdf();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void htmlToPdf() throws Exception {
		List<String> fNames = this.getFileNames();
		 for (String a : fNames) {
		String fileName = a;

		InputStream bodyIs = new FileInputStream("store\\result\\" + fileName);
		// InputStream cssIs = new FileInputStream("");
		String body = this.getContent(bodyIs);
		try {
			String content = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" /></head><body>"
					+ body + "</body></html>";
			InputStream is = new ByteArrayInputStream(content.getBytes("GBK"));
			// step 1
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream("store\\word\\flfg\\" + fileName + ".pdf"));
			// step 3
			document.open();
			// step 4
			XMLWorkerFontProvider fontImp = new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS);	

			XMLWorkerHelper.getInstance().parseXHtml(writer, document, is, Charset.forName("GBK"));
			//XMLWorkerHelper.getInstance().parseXHtml(writer, document, is, null, Charset.forName("GBK"),fontImp);
			// step 5
			document.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
