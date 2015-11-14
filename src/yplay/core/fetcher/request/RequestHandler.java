package yplay.core.fetcher.request;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.HttpResponseBodyPart;
import com.ning.http.client.Response;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

public abstract class RequestHandler extends AsyncCompletionHandler<Response> {
    protected ByteArrayOutputStream bytes = new ByteArrayOutputStream();

    @Override
    public STATE onBodyPartReceived(HttpResponseBodyPart bodyPart) throws Exception {
        bytes.write(bodyPart.getBodyPartBytes());
        return STATE.CONTINUE;
    }

    @Override
    public void onThrowable(Throwable t) {
        System.out.print(t.getMessage());
    }

    protected String getResponseText() throws UnsupportedEncodingException {
        return bytes.toString("UTF-8");
    }
}
