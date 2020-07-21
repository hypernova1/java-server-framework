package org.sam.server.http;

import org.sam.server.constant.ContentType;
import org.sam.server.constant.HttpMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by melchor
 * Date: 2020/07/17
 * Time: 1:34 PM
 */
public class Request {

    private final String path;
    private final HttpMethod method;
    private final Map<String, String> headers;
    private final Map<String, String> parameterMap;
    private final List<Cookie> cookies;
    private final String json;

    private Request(String path, HttpMethod method, Map<String, String> headers, Map<String, String> parameterMap, String json, List<Cookie> cookies) {
        this.path = path;
        this.method = method;
        this.headers = headers;
        this.parameterMap = parameterMap;
        this.json = json;
        this.cookies = cookies;
    }

    public static Request create(BufferedReader br) {
        UrlParser urlParser = new UrlParser(br);

        Map<String, String> headers = urlParser.headers;
        HttpMethod method = urlParser.method;
        String path = urlParser.path;
        Map<String, String> parameters = urlParser.parameters;
        List<Cookie> cookies = urlParser.cookies;

        return new Request(path, method, headers, parameters, urlParser.json, cookies);
    }

    public String getPath() {
        return this.path;
    }

    public HttpMethod getMethod() {
        return this.method;
    }

    public String getParameter(String key) {
        return this.parameterMap.get(key);
    }

    public Map<String, String> getParameters() {
        return this.parameterMap;
    }

    public Set<String> getParameterNames() {
        return this.parameterMap.keySet();
    }

    public Set<String> getHeaderNames() {
        return headers.keySet();
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public String getJson() {
        return this.json;
    }

    public List<Cookie> getCookies() {
        return this.cookies;
    }

    private static class UrlParser {
        private String path;
        private HttpMethod method;
        private Map<String, String> headers = new HashMap<>();
        private Map<String, String> parameters = new HashMap<>();
        private List<Cookie> cookies = new ArrayList<>();
        private String json;

        public UrlParser(BufferedReader br) {
            try {
                String input = br.readLine();
                StringTokenizer parse = new StringTokenizer(input);
                String method = parse.nextToken().toUpperCase();
                String requestPath = parse.nextToken().toLowerCase();

                String rawParameter = parsePath(requestPath);
                StringBuilder rawParameters = new StringBuilder(rawParameter);
                parseHeaders(br);
                parseMethod(method);

                String requestBody;
                if (HttpMethod.get(method).equals(HttpMethod.POST) ||
                        HttpMethod.get(method).equals(HttpMethod.PUT) ||
                        ContentType.JSON.getValue().equals(headers.get("content-type"))) {
                    while ((requestBody = br.readLine()) != null) {
                        rawParameters.append(requestBody);
                    }

                    String s = headers.get("content-type");
                    if (ContentType.JSON.getValue().equals(headers.get("content-type"))) {
                        this.json = rawParameters.toString();
                        return;
                    }
                }

                if (!rawParameters.toString().equals("")) {
                    parseParameters(rawParameters.toString());
                }

            } catch (IOException e) {
                System.out.println("terminate thread..");
                e.printStackTrace();
            }
        }

        private void parseHeaders(BufferedReader br) {
            try {
                String s = br.readLine();
                while (!s.trim().equals("")) {
                    int index = s.indexOf(": ");
                    String key = s.substring(0, index).toLowerCase();
                    String value = s.substring(index + 2);
                    if ("cookie".equals(key)) {
                        CookieStore cookieStore = new CookieStore();
                        this.cookies = cookieStore.parseCookie(value);
                        s = br.readLine();
                        continue;
                    }

                    this.headers.put(key, value);
                    s = br.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void parseMethod(String method) {
            this.method = HttpMethod.get(method);
        }

        private String parsePath(String requestPath) {
            this.path = requestPath;
            int index = path.indexOf("?");
            if (index != -1) {
                this.path = requestPath.substring(0, index);
                return requestPath.substring(index + 1);
            }
            return "";
        }

        private void parseParameters(String parameters) {
            if (parameters.startsWith("{")) {
                return;
            }
            String[] rawParameters = parameters.split("&");
            Arrays.stream(rawParameters).forEach(parameter -> {
                String[] parameterPair = parameter.split("=");
                String name = parameterPair[0];
                String value = null;
                if (parameterPair.length == 2) {
                    value = parameterPair[1];
                }
                this.parameters.put(name, value);
            });
        }
    }

}
