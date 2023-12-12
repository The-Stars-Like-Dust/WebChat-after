package com.example.springbootchat.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebFilter("/*")
@Component
public class filter implements Filter {
    static BufferedWriter out;
    static DateFormat df;
    static Date date;
    static File file;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        try {
            file = new File("remoteAddr.log");
            out = new BufferedWriter(new FileWriter(file));
            df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = new Date();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        date.setTime(System.currentTimeMillis());
        out.write(servletRequest.getRemoteHost() + ":" + servletRequest.getRemotePort() + "-----" + df.format(date));
        out.newLine();
        out.flush();
        if (file.length() > 1024 * 1024 * 10) {
            try {
                out.close();
                file.delete();
                out = new BufferedWriter(new FileWriter(file));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);

    }

    @Override
    public void destroy() {
        try {
            out.close();
            date = null;
            df = null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
