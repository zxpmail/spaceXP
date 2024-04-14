package cn.piesat.framework.web.xss;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;


import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class XssFilter implements Filter {
	/**
	 * 排除链接
	 */
	AntPathMatcher matcher = new AntPathMatcher();
	public Set<String> excludes = new HashSet<>();
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String tempExcludes = filterConfig.getInitParameter("excludes");
		if (StringUtils.hasText(tempExcludes)) {
			String[] excludedUrls = tempExcludes.split(",", -1);
			excludes = Arrays.stream(excludedUrls)
					.filter(StringUtils::hasText)
					.map(String::trim)
					.collect(Collectors.toSet());
		}
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		if (handleExcludeURL(req)) {
			chain.doFilter(request, response);
			return;
		}
		XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper((HttpServletRequest) request);
		chain.doFilter(xssRequest, response);
	}
	private boolean handleExcludeURL(HttpServletRequest request) {
		String url = request.getServletPath();
		String method = request.getMethod();
		// GET DELETE 不过滤
		if (method == null ) {
			return true;
		}
		return matches(url, excludes);
	}

	private boolean matches(String url, Set<String> excludes) {
		if(!StringUtils.hasText(url)){
			return false;
		}
		for (String pattern : excludes) {
			return matcher.match(pattern, url);
		}
		return false;
	}

	@Override
	public void destroy() {
	}

}