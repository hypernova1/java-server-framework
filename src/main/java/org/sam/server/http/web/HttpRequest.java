package org.sam.server.http.web;

import org.sam.server.constant.HttpMethod;
import org.sam.server.http.Cookie;
import org.sam.server.http.context.HttpServer;
import org.sam.server.http.Session;

import java.util.Map;
import java.util.Set;

/**
 * Request 인터페이스의 구현체입니다. 일반적인 HTTP 요청에 대한 정보를 저장합니다.
 *
 * @author hypernova1
 * @see Request
 */
public class HttpRequest implements Request {

    private final String protocol;

    private final String path;

    private final HttpMethod method;

    private final Map<String, String> headers;

    private final Map<String, String> parameterMap;

    private final String json;

    private final Set<Cookie> cookies;

    protected HttpRequest(RequestParser requestParser) {
        this.protocol = requestParser.protocol;
        this.path = requestParser.url;
        this.method = requestParser.httpMethod;
        this.headers = requestParser.headers;
        this.parameterMap = requestParser.parameters;
        this.json = requestParser.json;
        this.cookies = requestParser.cookies;
    }

    @Override
    public String getProtocol() {
        return this.protocol;
    }

    @Override
    public String getUrl() {
        return this.path;
    }

    @Override
    public HttpMethod getMethod() {
        return this.method;
    }

    @Override
    public String getParameter(String key) {
        return this.parameterMap.get(key);
    }

    @Override
    public Map<String, String> getParameters() {
        return this.parameterMap;
    }

    @Override
    public Set<String> getParameterNames() {
        return this.parameterMap.keySet();
    }

    @Override
    public Set<String> getHeaderNames() {
        return headers.keySet();
    }

    @Override
    public String getHeader(String key) {
        return headers.get(key);
    }

    @Override
    public String getJson() {
        return json;
    }

    @Override
    public Set<Cookie> getCookies() {
        return this.cookies;
    }

    @Override
    public Session getSession() {
        for (Cookie cookie : cookies) {
            if (!cookie.getName().equals("sessionId")) continue;
            return HttpServer.SessionManager.getSession(cookie.getValue());
        }
        return new Session();
    }

}