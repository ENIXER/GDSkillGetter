package jp.enixer.gdskillgetter.helper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import jp.enixer.gdskillgetter.internal.Config;
import jp.enixer.gdskillgetter.model.Music;
import jp.enixer.gdskillgetter.types.Type;

public class CSVOutputter {
	public static void output(List<Music> musics) {
		try {
			File file = new File("output.csv");
			FileOutputStream stream = new FileOutputStream(file);
			Writer writer = new BufferedWriter(new OutputStreamWriter(stream,
					"utf-8"));
			CSVPrinter printer = CSVFormat.DEFAULT.withHeader("music_id",
					"kind", "rate", "comment", "isfc").print(writer);
			for (Type type : new Type[] { Type.G, Type.D }) {
				if (Config.canUpdate(type)) {
					Collections.sort(musics, new MusicComparator(type));
					int size = musics.size();
					if (Config.canUpdateOnlySkillTarget()) {
						size = 25;
					}
					int hot = 0, other = 0;
					for (Music music : musics) {
						if (music.isNew()) {
							if (hot >= size) {
								continue;
							}
							hot++;
						} else {
							if (other >= size) {
								continue;
							}
							other++;
						}
						List<String> outputs = music.getOutputs(type);
						if (outputs != null) {
							printer.printRecord(outputs);
							printer.flush();
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static class MusicComparator implements Comparator<Music> {
		private Type type;

		private MusicComparator(Type type) {
			this.type = type;
		}

		@Override
		public int compare(Music o1, Music o2) {
			if (o1.isNew() && !o2.isNew()) {
				return -1;
			}
			if (!o1.isNew() && o2.isNew()) {
				return 1;
			}
			double diff = o1.getTargetSkillPoint(type)
					- o2.getTargetSkillPoint(type);
			if (diff > 0) {
				return -1;
			}
			if (diff < 0) {
				return 1;
			}
			return 0;
		}

	}
}
