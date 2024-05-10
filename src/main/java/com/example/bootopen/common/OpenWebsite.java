package com.example.bootopen.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

/**
 * @description 启动后自动打开网站
 * @auth chaijd
 * @date 2023/6/12
 */
@Component
@Slf4j
public class OpenWebsite implements CommandLineRunner {
    private static final String[] WINDOWS_CMD = {"cmd", "/c", "start"};
    private static final String[] LINUX_CMD = {"/usr/bin/xdg-open"};

    @Override
    public void run(String... args) throws IOException, URISyntaxException {
        String url = "http://localhost:8089";
        url = "http://127.0.0.1:8089/druid/index.html";
        if (Desktop.isDesktopSupported()) {  // 检查是否支持桌面操作
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {  // 检查是否支持浏览器打开
                desktop.browse(new URI(url));
            } else {
                openUrlWithCommand(url);
            }
        } else {
            openUrlWithCommand(url);
        }
    }

    private void openUrlWithCommand(String url) throws IOException {
        String[] cmd;
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            cmd = WINDOWS_CMD;
        } else {
            cmd = LINUX_CMD;
        }
        cmd = Arrays.copyOf(cmd, cmd.length + 1);
        cmd[cmd.length - 1] = url;
        log.info("openUrlWithCommand cmd={}", Arrays.asList(cmd));
        Runtime.getRuntime().exec(cmd);
    }
}
