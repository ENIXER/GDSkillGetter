package jp.enixer.gdskillgetter;

import java.util.Map;
import java.util.Map.Entry;

import jp.enixer.gdskillgetter.helper.CSVOutputer;
import jp.enixer.gdskillgetter.helper.EAGateHelper;
import jp.enixer.gdskillgetter.helper.SkillNoteHelper;
import jp.enixer.gdskillgetter.internal.Config;
import jp.enixer.gdskillgetter.internal.HttpClientWrapper;
import jp.enixer.gdskillgetter.model.Music;
import jp.enixer.gdskillgetter.model.Result;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GDSkillGetter {
	private static final Log log = LogFactory.getLog(GDSkillGetter.class);
	
	private HttpClientWrapper eagate;
	private boolean started = false;
	protected HttpClientWrapper skillnote;

	public void run() {
		log.info("GDSkillGetterを起動します.");
		start();
		if (Config.canUpdateGf()) {
			Map<String, Result> gfResults = getGFAllMusics();
			CSVOutputer.output(gfResults, "gf");
		}
		if (Config.canUpdateDm()) {
			Map<String, Result> dmResults = getDMAllMusics();
			CSVOutputer.output(dmResults, "dm");
		}
	}
	
	public void start() {
		eagate = new HttpClientWrapper("Windows-31J");
		skillnote = new HttpClientWrapper("UTF-8");
		EAGateHelper.loginEAGate(eagate);
		started = true;
	}

	public Map<String, Result> getGFAllMusics() {
		if (!started) {
			return null;
		}
		Map<String, Music> musics = SkillNoteHelper.loadMusics(skillnote);
		Map<String, Result> results = EAGateHelper.getGfAllResult(eagate,
				musics, Config.canUpdateFullcombo());
		mergeResult(results, musics);
//		SkillNoteHelper.mergeGfSkillNoteResult(skillnote, config, results);

		return results;
	}

	public Map<String, Result> getDMAllMusics() {
		if (!started) {
			return null;
		}
		Map<String, Music> musics = SkillNoteHelper.loadMusics(skillnote);
		Map<String, Result> results = EAGateHelper.getDmAllResult(eagate,
				musics, Config.canUpdateFullcombo());
		mergeResult(results, musics);
//		SkillNoteHelper.mergeDmSkillNoteResult(skillnote, config, results);

		return results;
	}

	public void mergeResult(Map<String, Result> results,
			Map<String, Music> musics) {
		for (Entry<String, Result> _result : results.entrySet()) {
			Result result = _result.getValue();
			for (Entry<String, Music> entry : musics.entrySet()) {
				Music music = entry.getValue();
				if (StringUtils.equals(music.name, result.music.name)) {
					result.music.skillnoteId = music.skillnoteId;
					result.music.skillnoteName = music.skillnoteName;
					result.music.isNew = music.isNew;
					result.music.difficulties.addAll(music.difficulties);
				}
			}
		}
	}

}
