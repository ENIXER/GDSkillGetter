package jp.enixer.gdskillgetter.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.enixer.gdskillgetter.internal.Config;
import jp.enixer.gdskillgetter.types.Type;

public class Results {

	private ArrayList<Result> list = new ArrayList<Result>();

	public double getSkillTargetPoint(Type type) {
		Result result = getSkillTarget(type);
		if (result == null) {
			return 0;
		}
		return result.getSkillPoint();
	}

	private Result getSkillTarget(Type type) {
		Collections.sort(list);
		for (Result result : list) {
			if (result.isModel(type)) {
				return result;
			}
		}
		return null;
	}

	public void merge(LevelData level, ResultData result) {
		Result res = new Result();
		res.merge(level, result);
		list.add(res);
	}

	public List<String> getOutputs(Type type) {
		Result target = getSkillTarget(type);
		if (target == null) {
			return null;
		}
		List<String> output = new ArrayList<String>(5);
		output.add(target.getKindString());
		output.add(target.getAchievementRate());
		StringBuilder builder = new StringBuilder();
		if (Config.canAddSkillInfoToComment()) {
			for (Result result : list) {
				if (result.isModel(type) && result != target) {
					builder.append(result.toCSVString());
					builder.append(", ");
				}
			}
		}
		output.add(builder.length() == 0 ? "" : builder.substring(0,
				builder.length() - 2));
		output.add(target.isFullcombo());
		return output;
	}

}
