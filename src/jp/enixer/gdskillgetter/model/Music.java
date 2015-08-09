package jp.enixer.gdskillgetter.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Music implements Serializable {

	private static final long serialVersionUID = 1L;

	public String name;

	public String eagateName;

	public String skillnoteName;

	public String skillnoteId;

	public boolean isNew;

	public Set<Difficulty> difficulties = new HashSet<Difficulty>();

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}

}
