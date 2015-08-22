package jp.enixer.gdskillgetter.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Results implements Comparable<Results> {

	private ArrayList<Result> list = new ArrayList<Result>();

	private double getSkillTargetPoint() {
		Collections.sort(list);
		return list.get(0).getSkillPoint();
	}
	
	private Result getGfSkillTarget(){
		Collections.sort(list);
		for(Result result:list){
			if(result.isGf()){
				return result;
			}
		}
		return null;
	}

	private Result getDmSkillTarget() {
		Collections.sort(list);
		for(Result result:list){
			if(result.isDm()){
				return result;
			}
		}
		return null;
	}
	
	@Override
	public int compareTo(Results o) {
		if (o == null) {
			return -1;
		}
		double diff = o.getSkillTargetPoint() - getSkillTargetPoint();
		if (diff < 0) {
			return -1;
		}
		if (diff > 0) {
			return 1;
		}
		return 0;
	}

	public void merge(LevelData level, ResultData result) {
		Result res = new Result();
		res.merge(level, result);
		list.add(res);
	}

	public List<String> getGfAllOutputs() {
		Result gfResult = getGfSkillTarget();
		if(gfResult == null){
			return null;
		}
		return gfResult.getOutputs();
	}

	public List<String> getDmAllOutputs() {
		Result dmResult = getDmSkillTarget();
		if(dmResult == null){
			return null;
		}
		return dmResult.getOutputs();
	}

}
