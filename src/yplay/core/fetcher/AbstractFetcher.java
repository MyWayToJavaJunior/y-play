package yplay.core.fetcher;

import com.ning.http.client.AsyncHttpClient;

public abstract class AbstractFetcher {
    protected AsyncHttpClient http = new AsyncHttpClient();
}
