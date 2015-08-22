package jp.enixer.gdskillgetter.helper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import jp.enixer.gdskillgetter.model.Music;

public class CSVOutputter {
	public static void output(List<Music> musics) {
		try {
			File file = new File("output.csv");
			FileOutputStream stream = new FileOutputStream(file);
			Writer writer = new BufferedWriter(new OutputStreamWriter(stream,
					"utf-8"));
			CSVPrinter printer = CSVFormat.DEFAULT.withHeader("music_id",
					"kind", "rate", "comment", "isfc").print(writer);
			for (Music music : musics) {
				List<List<String>> outputs = music.getOutputs();
				for (List<String> output : outputs)
					printer.printRecord(output);
				printer.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
