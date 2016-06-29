/*
 * *****************************************************************************
 *      Cloud Foundry
 *      Copyright (c) [2009-2016] Pivotal Software, Inc. All Rights Reserved.
 *      This product is licensed to you under the Apache License, Version 2.0 (the "License").
 *      You may not use this product except in compliance with the License.
 *
 *      This product includes a number of subcomponents with
 *      separate copyright notices and license terms. Your use of these
 *      subcomponents is subject to the terms and conditions of the
 *      subcomponent's license, as noted in the LICENSE file.
 * *****************************************************************************
 */

package org.cloudfoundry.identity.uaa.authentication;

import org.springframework.http.MediaType;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static org.cloudfoundry.identity.uaa.util.UaaStringUtils.ISO_8859_1;
import static org.cloudfoundry.identity.uaa.util.UaaStringUtils.convertISO8859_1_to_UTF_8;

public class UTF8ConversionFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)res;
        //application/x-www-form-urlencoded is always considered ISO-8859-1 by tomcat even when
        //because there is no charset defined
        //the browser sends up UTF-8
        //https://www.w3.org/TR/html5/forms.html#application/x-www-form-urlencoded-encoding-algorithm
        if (MediaType.APPLICATION_FORM_URLENCODED_VALUE.equals(request.getContentType()) &&
            (request.getCharacterEncoding() == null ||  ISO_8859_1.equalsIgnoreCase(request.getCharacterEncoding()))
           ) {
            chain.doFilter(new UtfConverterRequestWrapper(request), response);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }

    public static class UtfConverterRequestWrapper extends HttpServletRequestWrapper {
        public UtfConverterRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getParameter(String name) {
            return convertISO8859_1_to_UTF_8(super.getParameter(name));
        }

        @Override
        public String[] getParameterValues(String name) {
            String[] values = super.getParameterValues(name);
            if (values==null || values.length==0) {
                return values;
            }
            String[] result = new String[values.length];
            for (int i=0; i<result.length; i++) {
                result[i] = convertISO8859_1_to_UTF_8(values[i]);
            }
            return result;
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            Map<String,String[]> map = new HashMap<>();
            Enumeration<String> names = getParameterNames();

            while (names.hasMoreElements()) {
                String name = names.nextElement();
                map.put(name, getParameterValues(name));
            }
            return map;
        }
    }
}
