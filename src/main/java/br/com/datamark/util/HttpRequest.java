package br.com.datamark.util;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.net.URI;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private CloseableHttpClient httpClient;
    private CloseableHttpResponse httpResponse = null;
    private HttpClientContext clientContext = HttpClientContext.create();
    private BasicCookieStore cookieStore = new BasicCookieStore();

    public HttpRequest() {
        this.initConfig();
    }

    public String getUrl(String url) {
        try {
            HttpUriRequest request = RequestBuilder.get()
                    .setUri(new URI(url))
                    .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                    .addHeader("Accept-Encoding", "gzip,deflate,br")
                    .addHeader("Accept-Language", "pt-BR,pt;q=0.8,en-US;q=0.5,en;q=0.3")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.111 Safari/537.36")
                    .build();
            return this.getResponse(request);
        }
        catch (Exception except) {
            except.printStackTrace();
        }
        return null;
    }

    public String getUrl(String url, NameValuePair[] params) {
        try {
            HttpUriRequest request = RequestBuilder.post()
                    .setUri(new URI(url))
                    .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                    .addHeader("Accept-Encoding", "gzip,deflate,br")
                    .addHeader("Accept-Language", "pt-BR,pt;q=0.8,en-US;q=0.5,en;q=0.3")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.111 Safari/537.36")
                    .addParameters(params)
                    .build();
            return this.getResponse(request);
        }
        catch (Exception except) {
            except.printStackTrace();
        }
        return null;
    }

    public String getUrl(String url, NameValuePair[] postParam, HashMap<String, String> headers) {
        try {
            RequestBuilder builder = RequestBuilder.post();
            builder.setUri(new URI(url));
            builder.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            builder.addHeader("Accept-Encoding", "gzip,deflate,br");
            builder.addHeader("Accept-Language", "pt-BR,pt;q=0.8,en-US;q=0.5,en;q=0.3");
            builder.addHeader("Connection", "keep-alive");
            builder.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.111 Safari/537.36");
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
            builder.addParameters(postParam);
            HttpUriRequest request = builder.build();
            return this.getResponse(request);
        }
        catch (Exception except) {
            except.printStackTrace();
        }
        return null;
    }

    private String getResponse(HttpUriRequest request) {
        try {
            this.clientContext.setCookieStore(this.cookieStore);
            this.httpResponse = this.httpClient.execute(request, this.clientContext);
            int status = this.httpResponse.getStatusLine().getStatusCode();
            if (status == 200) {
                HttpEntity entity = this.httpResponse.getEntity();
                return EntityUtils.toString(entity);
            }
            return Integer.toString(status);
        }
        catch (Exception except) {
            except.printStackTrace();
        }
        finally {
            try {
                if (this.httpResponse != null) {
                    this.httpResponse.close();
                }
            }
            catch (Exception except) {
                except.printStackTrace();
            }
        }
        return null;
    }

    private void initConfig() {
        this.httpClient = HttpClients.custom()
                .setSSLContext(this.getSSLContext())
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .setDefaultRequestConfig(this.getRequestConfig())
                .setRedirectStrategy(new LaxRedirectStrategy())
                .setDefaultCookieStore(this.cookieStore)
                .setConnectionManagerShared(true)
                .build();
    }

    private SSLContext getSSLContext() {
        try {
            return SSLContexts.custom()
                    .loadTrustMaterial(null, (TrustStrategy) (X509Certificate[] xcs, String string) -> true).build();
        }
        catch (Exception except) {
            except.printStackTrace();
        }
        return null;
    }

    private RequestConfig getRequestConfig() {
        return RequestConfig.custom()
                .setConnectTimeout(50000)
                .setConnectionRequestTimeout(50000)
                .setSocketTimeout(50000)
                .build();
    }
}