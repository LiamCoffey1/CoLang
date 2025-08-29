package colang.webserver;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpUtil;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderValues.CLOSE;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpHeaderValues.TEXT_PLAIN;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import colang.interperter.CodeInterperter;
import colang.interperter.CodeExcecutor.CodeExcecutor;
import colang.interperter.CodeExcecutor.implementations.CLCodeExecutor;
import colang.interperter.CodeOptimizer.CodeOptimzer;
import colang.interperter.CodeOptimizer.implementations.CLCodeOptimizer;
import colang.interperter.SyntaxTreeGenerator.SyntaxTreeGenerator;
import colang.interperter.SyntaxTreeGenerator.implementations.CLSyntaxTreeGenerator;

public class ServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    private static String previous = "init";

    // Configurable web root: -Dcolang.webroot=C:\\path\\to\\dist or env COLANG_WEBROOT
    private static final Path WEB_ROOT = resolveWebRoot();

    private static Path resolveWebRoot() {
        String fromProp = System.getProperty("colang.webroot");
        if (fromProp != null && !fromProp.isBlank()) {
            return Paths.get(fromProp).toAbsolutePath().normalize();
        }
        String fromEnv = System.getenv("COLANG_WEBROOT");
        if (fromEnv != null && !fromEnv.isBlank()) {
            return Paths.get(fromEnv).toAbsolutePath().normalize();
        }
        // Default to a local "dist" directory under the current working directory
        return Paths.get(System.getProperty("user.dir"), "dist").toAbsolutePath().normalize();
    }

    private static Path resolvePath(String uri) {
        // strip query string and leading slash
        String clean = uri;
        int q = clean.indexOf('?');
        if (q >= 0) clean = clean.substring(0, q);
        if (clean.startsWith("/")) clean = clean.substring(1);
        Path candidate = WEB_ROOT.resolve(clean).normalize();
        if (!candidate.startsWith(WEB_ROOT)) {
            throw new SecurityException("Attempted path traversal: " + uri);
        }
        return candidate;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, HttpObject msg) {
        String content = "";
        if (msg instanceof HttpRequest) {
            HttpRequest req = (HttpRequest) msg;
           System.out.println(req.uri());
           if(req.uri().contains(".html")) {
                try {
                    Path file = resolvePath(req.uri());
                    content = Files.readString(file, StandardCharsets.UTF_8);
                } catch (IOException | SecurityException e) {
                    e.printStackTrace();
                    content = "<html><body><h1>Not Found</h1></body></html>";
                }
           }
           if(req.uri().contains(".cl")) {
            
            StringBuilder contentBuilder = new StringBuilder();
            try {
                Path file = resolvePath(req.uri());
                // Preserve original behavior of adding spaces between lines
                for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
                    contentBuilder.append(line).append(' ');
                }
            } catch (IOException | SecurityException e) {
                e.printStackTrace();
            }
            content = contentBuilder.toString();
            System.out.print(previous.toString());
            previous = content;
            SyntaxTreeGenerator treeGenerator = new CLSyntaxTreeGenerator();
            CodeOptimzer optimizer = new CLCodeOptimizer();
            CodeExcecutor excecutor = new CLCodeExecutor();
            CodeInterperter interperter = new CodeInterperter(treeGenerator, optimizer, excecutor);
            interperter.interpert(content);
            content = HTMLBuilder.getInstance().getAndDispose();
       }   
            boolean keepAlive = HttpUtil.isKeepAlive(req);
            FullHttpResponse response = new DefaultFullHttpResponse(req.protocolVersion(), OK,
                                                                    Unpooled.wrappedBuffer(content.getBytes()));
            response.headers()
                    .set(CONTENT_TYPE, "text/html")
                    .setInt(CONTENT_LENGTH, response.content().readableBytes());

            if (keepAlive) {
                if (!req.protocolVersion().isKeepAliveDefault()) {
                    response.headers().set(CONNECTION, KEEP_ALIVE);
                }
            } else {
                // Tell the client we're going to close the connection.
                response.headers().set(CONNECTION, CLOSE);
            }

            ChannelFuture f = ctx.write(response);

            if (!keepAlive) {
                f.addListener(ChannelFutureListener.CLOSE);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}