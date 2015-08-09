package jp.enixer.gdskillgetter.model;

import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Result implements Comparable<Result>{

	private static final long serialVersionUID = 1L;

	public Music music;

	public SkillNoteResult skillNoteResult;

	public Set<ResultDetail> details = new TreeSet<ResultDetail>();

	public ResultDetail getSkillTargetDetail() {
		if (!((TreeSet<ResultDetail>) details).isEmpty()) {
			return ((TreeSet<ResultDetail>) details).first();
		}
		return null;
	}

	public int compareTo(Result o) {
		if (o == null) {
			return 1;
		}
		ResultDetail o1 = getSkillTargetDetail();
		ResultDetail o2 = o.getSkillTargetDetail();
		if (o1 == null && o2 == null) {
			return 0;
		}
		if (o1 != null && o2 == null) {
			return 1;
		}
		if (o1 == null && o2 != null) {
			return -1;
		}
		return o1.compareTo(o2);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}


}
