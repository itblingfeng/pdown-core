package io.pdown.core.entity;

import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import io.pdown.core.util.ProtoUtil.RequestProto;
import java.io.Serializable;
import java.util.Map.Entry;

public class HttpRequestInfo implements HttpRequest, Serializable {

  private static final long serialVersionUID = -4521453515739581677L;
  private RequestProto requestProto;
  private HttpVer version;
  private String method;
  private String uri;
  private HttpHeadsInfo headers;
  private byte[] content;

  public HttpRequestInfo() {
  }

  public HttpRequestInfo(HttpVer version, HttpMethod method, String uri) {
    this(version, method, uri, null, null);
  }

  public HttpRequestInfo(HttpMethod method, String uri) {
    this(HttpVer.HTTP_1_1, method, uri);
  }

  public HttpRequestInfo(HttpVer version, HttpMethod method, String uri,
      HttpHeadsInfo headers, byte[] content) {
    this.version = version;
    this.method = method.toString();
    this.uri = uri;
    this.headers = headers;
    this.content = content;
  }

  public HttpRequest setContent(byte[] content) {
    this.content = content;
    return this;
  }

  public byte[] content() {
    return content;
  }

  public RequestProto requestProto() {
    return requestProto;
  }

  public HttpRequest setRequestProto(RequestProto requestProto) {
    this.requestProto = requestProto;
    return this;
  }

  @Override
  public HttpMethod method() {
    return new HttpMethod(method);
  }

  @Override
  @Deprecated
  public HttpMethod getMethod() {
    return method();
  }

  @Override
  public HttpRequest setMethod(HttpMethod method) {
    this.method = method.toString();
    return this;
  }

  @Override
  public String uri() {
    return uri;
  }

  @Override
  @Deprecated
  public String getUri() {
    return uri();
  }

  @Override
  public HttpRequest setUri(String uri) {
    this.uri = uri;
    return this;
  }


  @Override
  public HttpVersion protocolVersion() {
    if (version == HttpVer.HTTP_1_0) {
      return HttpVersion.HTTP_1_0;
    } else {
      return HttpVersion.HTTP_1_1;
    }
  }

  @Deprecated
  @Override
  public HttpVersion getProtocolVersion() {
    return protocolVersion();
  }

  @Override
  public HttpRequest setProtocolVersion(HttpVersion version) {
    if (version.minorVersion() == 0) {
      this.version = HttpVer.HTTP_1_0;
    } else {
      this.version = HttpVer.HTTP_1_1;
    }
    return this;
  }

  @Override
  public HttpHeaders headers() {
    return headers;
  }

  public HttpRequest setVersion(HttpVer version) {
    this.version = version;
    return this;
  }

  public HttpRequest setMethod(String method) {
    this.method = method;
    return this;
  }

  public HttpRequest setHeaders(HttpHeadsInfo headers) {
    this.headers = headers;
    return this;
  }

  @Deprecated
  @Override
  public DecoderResult getDecoderResult() {
    return null;
  }

  @Override
  public DecoderResult decoderResult() {
    return null;
  }

  @Override
  public void setDecoderResult(DecoderResult result) {

  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder();
    buf.append(method());
    buf.append(' ');
    buf.append(uri());
    buf.append(' ');
    buf.append(protocolVersion());
    buf.append('\n');
    if (headers != null) {
      for (Entry<String, String> e : headers) {
        buf.append(e.getKey());
        buf.append(": ");
        buf.append(e.getValue());
        buf.append('\n');
      }
      buf.setLength(buf.length() - 1);
    }
    if (content != null) {
      buf.append(new String(content));
    }
    return buf.toString();
  }

  public enum HttpVer {
    HTTP_1_0, HTTP_1_1
  }

  public static HttpRequest adapter(HttpRequest httpRequest) {
    if (httpRequest instanceof DefaultHttpRequest) {
      HttpVer version;
      if (httpRequest.protocolVersion().minorVersion() == 0) {
        version = HttpVer.HTTP_1_0;
      } else {
        version = HttpVer.HTTP_1_1;
      }
      HttpHeadsInfo httpHeadsInfo = new HttpHeadsInfo();
      for (Entry<String, String> entry : httpRequest.headers()) {
        httpHeadsInfo.set(entry.getKey(), entry.getValue());
      }
      return new HttpRequestInfo(version, httpRequest.method(), httpRequest.uri(),
          httpHeadsInfo, null);
    }
    return httpRequest;
  }
}
