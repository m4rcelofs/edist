package com.erep.dist.erep;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.esxx.js.protocol.GAEConnectionManager;

import com.erep.dist.data.Player;
import com.erep.dist.data.ResourceDist;

public class ErepScrapper implements Closeable {

	private static final Logger		log	= Logger.getLogger(ErepScrapper.class
												.getName());
	private final DefaultHttpClient	client;
	private final String			email;
	private final String			password;

	public ErepScrapper(String email, String password) {
		this.email = email;
		this.password = password;
		client = createNewHttpClient();
	}

	public String scrape(String url) throws IOException {
		HttpGet httpGet = new HttpGet(url);
		HttpResponse response = client.execute(httpGet);
		log.warning("main response");
		printHeaders(response);
		printCookies(client.getCookieStore().getCookies());
		return read(response);
	}

	public void sendItem(Player player, ResourceDist resource, String formToken)
			throws IOException {
		HttpPost httpPost = new HttpPost(eRepURL.DONATE_ITEMS_ACTION.getUrl(""));
		{
			List<NameValuePair> nvps = new ArrayList<>(5);
			nvps.add(new BasicNameValuePair("citizen_id", player.getId() + ""));
			nvps.add(new BasicNameValuePair("amount", resource.getQuantity()
					+ ""));
			nvps.add(new BasicNameValuePair("industry_id",
					resource.getType().industryId + ""));
			nvps.add(new BasicNameValuePair("quality", resource.getQuality()
					.formValue()));
			nvps.add(new BasicNameValuePair("_token", formToken));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
		}
		HttpResponse postResponse = client.execute(httpPost);
		printHeaders(postResponse);
		read(postResponse);
	}

	public void login() throws IOException {

		log.warning("GET response");
		HttpGet httpGet = new HttpGet("http://www.erepublik.com/en");

		HttpResponse getResponse = client.execute(httpGet);

		printHeaders(getResponse);
		printCookies(client.getCookieStore().getCookies());
		String html = read(getResponse);
		String hiddenToken = html
				.substring(
						html.indexOf("<input type=\"hidden\" id=\"_token\" name=\"_token\" value=\"") + 54,
						html.indexOf("<div class=\"visible_form\">") - 6);

		log.log(Level.WARNING, "Hidden token: " + hiddenToken);

		log.warning("Post response");
		HttpPost httpPost = new HttpPost("http://www.erepublik.com/en/email");

		List<NameValuePair> nvps = new ArrayList<>(4);
		nvps.add(new BasicNameValuePair("_token", hiddenToken));
		nvps.add(new BasicNameValuePair("citizen_email", email));
		nvps.add(new BasicNameValuePair("citizen_password", password));
		// nvps.add(new BasicNameValuePair("remember", "on"));

		httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
		HttpResponse postResponse = client.execute(httpPost);
		printHeaders(postResponse);

		String setCookieHeader = postResponse.getHeaders("Set-Cookie")[0]
				.getValue();
		log.warning("Set-Cookie header value: " + setCookieHeader);

		String erpkValue = setCookieHeader.substring(
				setCookieHeader.indexOf("erpk=") + 5,
				setCookieHeader.indexOf("; expires="));
		String erpkMidValue = setCookieHeader.substring(
				setCookieHeader.indexOf("erpk_mid=") + 9,
				setCookieHeader.lastIndexOf("; expires="));

		BasicClientCookie erpk = new BasicClientCookie("erpk", erpkValue);
		BasicClientCookie erpk_auth = new BasicClientCookie("erpk_auth", "1");
		BasicClientCookie erpk_mid = new BasicClientCookie("erpk_mid",
				erpkMidValue);

		for (BasicClientCookie c : new BasicClientCookie[] { erpk, erpk_auth,
				erpk_mid }) {
			c.setPath("/");
			c.setDomain(".erepublik.com");
		}

		client.getCookieStore().addCookie(erpk);
		client.getCookieStore().addCookie(erpk_auth);
		client.getCookieStore().addCookie(erpk_mid);

		printCookies(client.getCookieStore().getCookies());

		read(postResponse);

		log.warning("Final response");

		HttpResponse finalResponse = client.execute(httpGet);
		printHeaders(finalResponse);
		printCookies(client.getCookieStore().getCookies());
		read(finalResponse);
	}

	public void close() {
		client.getConnectionManager().shutdown();
	}

	private String read(HttpResponse response) throws IOException {
		String html = IOUtils.toString(response.getEntity().getContent(),
				"UTF-8");
		EntityUtils.consume(response.getEntity());
		return html;
	}

	private void printCookies(List<Cookie> cookies) {
		log.log(Level.WARNING,
				"Set of cookies:"
						+ StringUtils.join(cookies.iterator(), " ||| "));
	}

	private void printHeaders(HttpResponse response) {
		log.log(Level.WARNING,
				"Set of headers:"
						+ StringUtils.join(response.getAllHeaders(), " ||| "));
	}

	private DefaultHttpClient createNewHttpClient() {

		String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:15.0) Gecko/20100101 Firefox/15.0.1";

		DefaultHttpClient client = new DefaultHttpClient(
				new GAEConnectionManager());

		HttpParams params = client.getParams();

		params.setParameter(ClientPNames.COOKIE_POLICY,
				CookiePolicy.BROWSER_COMPATIBILITY);

		params.setParameter(CoreProtocolPNames.USER_AGENT, userAgent);

		HttpConnectionParams.setConnectionTimeout(params, 60000);
		HttpConnectionParams.setSoTimeout(params, 60000);

		return client;
	}

}
