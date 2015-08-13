package jp.enixer.gdskillgetter.helper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import jp.enixer.gdskillgetter.model.Music;
import jp.enixer.gdskillgetter.model.Result;
import jp.enixer.gdskillgetter.model.ResultDetail;

public class CSVOutputer {
	public static void output(Map<String, Result> results,
			String outputFilePrefix) {
		try {
			File file = new File(outputFilePrefix + "output.csv");
			FileOutputStream stream = new FileOutputStream(file);
			Writer writer = new BufferedWriter(new OutputStreamWriter(stream,
					"utf-8"));
			CSVPrinter printer = CSVFormat.DEFAULT.withHeader("music_id",
					"kind", "rate", "comment", "isfc").print(writer);
			for (Result result : results.values()) {
				Music music = result.music;
				for (ResultDetail detail : result.details) {
					List<String> line = new ArrayList<String>();
					line.add(music.skillnoteId);
					line.add(Integer.toString(detail.getKind()));
					line.add(detail.achievements.toPlainString());
					line.add("");
					line.add(Boolean.toString(detail.isFullcombo));
					printer.printRecord(line);
					printer.flush();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
