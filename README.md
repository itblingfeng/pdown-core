# HTTP high speed downloader
Using java NIO,fast down and easy to customize.
## Guide
### Create download task  
  To create a download task, need to construct a request and a config.
### Request  
Before downloading, must have a HTTP request.
#### Method
The default method is GET,We can also use POST,PUT...
#### Heads
The default request header refers to the following table.
  
key | value
---|---
Host | {host}
Connection | keep-alive
User-Agent | Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.75 Safari/537.36
Accept | text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8
Referer | {host}

We can customize the request header,when overriding the default value, the default request header will be replaced.  
*{host} is Domain name parsed from url*
#### Content
Set the request body,only support text format.
### Config
Download options.

field | default | desc 
---|---|---
filePath | {root} | Saved path
fileName | {name} | Saved file name
totalSize | 0(B) | The total size of the file to be downloaded
supportRange | false | Does the server support Accept-Ranges
connections | 16 | Concurrent connections
timeout | 30(S) | If no response during this time,will re-initiate connection.
retryCount | 5 | All connection download exceptions exceed this times,will be stop download.
autoRename | false | Automatic rename when checking to download directory with duplicate file.
speedLimit | 0(B/S) | Download speed limit.

## Demo
```
//Download a file with URL
HttpDownBootstrap.builder("http://127.0.0.1/static/test.zip")
          .build()
          .startDown();
//Download a file with a custom request
HttpDownBootstrap.builder()
          .request(new HttpRequestInfo(HttpMethod.GET, "http://127.0.0.1/static/test.zip"))
          .downConfig(new HttpDownConfigInfo().setSupportRange(true).setTotalSize(1024))
          .build()
          .startDown();
//Set download config
HttpDownBootstrap.builder("http://127.0.0.1/static/test.zip")
          .downConfig(new HttpDownConfigInfo().setConnections(32).setAutoRename(true).setSpeedLimit(1024*1024*5L))
          .build()
          .startDown();
//Set proxy config
HttpDownBootstrap.builder("http://127.0.0.1/static/test.zip")
          .proxyConfig(new ProxyConfig(ProxyType.HTTP,"127.0.0.1",8888))
          .build()
          .startDown();
//Set callback
HttpDownBootstrap.builder("http://127.0.0.1/static/test.zip")
          .callback(new ConsoleHttpDownCallback())
          .build()
          .startDown();
```
## Build
```
git clone git@github.com:proxyee-down-org/pdown-core.git
cd pdown-core
mvn clean package -Pexec
```
## Run

```
java -jar pdown.jar -h  #See help
java -jar pdown.jar http://127.0.0.1/static/test.zip  #Download with default configuration
```

