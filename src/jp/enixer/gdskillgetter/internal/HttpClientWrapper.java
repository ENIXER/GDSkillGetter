package jp.enixer.gdskillgetter.internal;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpClientWrapper {

	private static final Log log = LogFactory.getLog(HttpClientWrapper.class);

	private HttpClient client;

	private String encoding;

	public HttpClientWrapper(String encoding) {
		Config config = Config.getInstance();
		client = new DefaultHttpClient();
		this.encoding = encoding;
		if (StringUtils.isNotEmpty(config.getProxyHost())) {
			HttpHost proxy = new HttpHost(config.getProxyHost(),
					config.getProxyPort());
			client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
					proxy);
		}
	}

	public String GET(String url) {
		try {
			HttpGet request = new HttpGet(url);
			HttpResponse response = client.execute(request);
			String s = IOUtils.toString(response.getEntity().getContent(),
					encoding);
			log.debug(url + " を読み込んでいます。");
			EntityUtils.consume(response.getEntity());
			return s;
		} catch (IOException e) {
			log.error(url + " の読み込みに失敗しました。");
			throw new RuntimeException(e);
		}
	}

	public String POST(String url, RequestParams params) {
		try {
			HttpPost post = new HttpPost(url);
			post.addHeader("Referer", url);
			post.setEntity(params.createHttpEntity(encoding));
			HttpResponse response = client.execute(post);
			String s = IOUtils.toString(response.getEntity().getContent(),
					encoding);
			log.debug(url + " を読み込んでいます。");
			EntityUtils.consume(response.getEntity());
			return s;
		} catch (IOException e) {
			log.error(url + " の読み込みに失敗しました。");
			throw new RuntimeException(e);
		}
	}

	public void shutdown() {
		client.getConnectionManager().shutdown();
	}

}
