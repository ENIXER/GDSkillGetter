package jp.enixer.gdskillgetter.internal;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

public class RequestParams {

	private List<NameValuePair> params = new ArrayList<NameValuePair>();

	public RequestParams() {

	}

	public RequestParams(String name, Object value) {
		params.add(new BasicNameValuePair(name, Objects.toString(value)));
	}

	public RequestParams add(String name, Object value) {
		params.add(new BasicNameValuePair(name, Objects.toString(value)));
		return this;
	}

	public List<NameValuePair> getParams() {
		return params;
	}

	HttpEntity createHttpEntity(String encoding) {
		try {
			return new UrlEncodedFormEntity(params, encoding);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}


}
