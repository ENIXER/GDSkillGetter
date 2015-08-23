package jp.enixer.gdskillgetter;

import java.util.List;

import jp.enixer.gdskillgetter.helper.CSVOutputter;
import jp.enixer.gdskillgetter.helper.EAGateHelper;
import jp.enixer.gdskillgetter.helper.SkillNoteHelper;
import jp.enixer.gdskillgetter.internal.Config;
import jp.enixer.gdskillgetter.internal.HttpClientWrapper;
import jp.enixer.gdskillgetter.model.LevelData;
import jp.enixer.gdskillgetter.model.Music;
import jp.enixer.gdskillgetter.model.ResultData;
import jp.enixer.gdskillgetter.model.Musics;
import jp.enixer.gdskillgetter.util.LogMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GDSkillGetter {
	private static final Log log = LogFactory.getLog(GDSkillGetter.class);

	private HttpClientWrapper eagate;
	private boolean started = false;
	protected HttpClientWrapper skillnote;

	public void run() {
		log.info(LogMessage.startGDSkillGetter());
		start();
		List<LevelData> levelTable = SkillNoteHelper.loadMusics(skillnote);
		List<ResultData> resultTable = null;
		if (Config.canUpdateGf()) {
			List<ResultData> gfResults = getGFAllMusics();
			resultTable = gfResults;
		}
		if (Config.canUpdateDm()) {
			List<ResultData> dmResults = getDMAllMusics();
			if (resultTable == null) {
				resultTable = dmResults;
			} else {
				resultTable.addAll(dmResults);
			}
		}
		List<Music> musics = Musics.mergeTables(levelTable, resultTable);
		CSVOutputter.output(musics);
		end();
	}

	public void start() {
		Config.getPropertiesFromConfig();
		eagate = new HttpClientWrapper("Windows-31J");
		skillnote = new HttpClientWrapper("UTF-8");
		EAGateHelper.loginEAGate(eagate);
		started = true;
	}

	public void end() {
		started = false;
		EAGateHelper.logoutEAGate(eagate);
		eagate.shutdown();
		skillnote.shutdown();
	}

	public List<ResultData> getGFAllMusics() {
		if (!started) {
			return null;
		}
		List<ResultData> results = EAGateHelper.getGfAllResult(eagate,
				Config.canUpdateFullcombo());

		return results;
	}

	public List<ResultData> getDMAllMusics() {
		if (!started) {
			return null;
		}
		List<ResultData> results = EAGateHelper.getDmAllResult(eagate,
				Config.canUpdateFullcombo());

		return results;
	}

}
