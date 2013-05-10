package net.messze.valahol;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class COFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        servletResponse.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE");
        servletResponse.addHeader("Access-Control-Allow-Origin", "*");
        servletResponse.addHeader("Access-Control-Allow-Headers", "Content-Type");
        chain.doFilter(request, response);

    }

    @Override
    public void destroy() {

    }
}
