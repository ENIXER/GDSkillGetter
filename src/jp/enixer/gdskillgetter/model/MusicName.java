package jp.enixer.gdskillgetter.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class MusicName implements Serializable, Comparable<MusicName> {

	private static final long serialVersionUID = 1L;

	public String normalizedName;

	public String eagateName;

	public String skillnoteName;

	public boolean isNew;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}

	@Override
	public boolean equals(Object anotherMusic) {
		if (anotherMusic == null || !(anotherMusic instanceof MusicName)) {
			return false;
		}
		return normalizedName.equals(((MusicName) anotherMusic).normalizedName);
	}

	@Override
	public int compareTo(MusicName o) {
		if (o == null) {
			return -1;
		}
		return normalizedName.compareTo(o.normalizedName);
	}

	public boolean isName(LevelData level) {
		return level.isName(normalizedName);
	}

	public void merge(LevelData level, ResultData result) {
		skillnoteName = level.getMusicName();
		normalizedName = level.getNormalizedName();
		isNew = level.isNew();
		eagateName = result.getMusicName();
	}

}
