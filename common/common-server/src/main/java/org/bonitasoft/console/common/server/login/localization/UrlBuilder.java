/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.console.common.server.login.localization;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Vincent Elcrin
 * 
 *         Should be done with hash maps...
 */
public class UrlBuilder {

    private String url;

    private static final String PARAM_TAG = "?";

    private static final String HASH_TAG = "#";

    private String hashParameters = new String();

    public UrlBuilder(final String url) {
        final String[] split = url.split(HASH_TAG);
        this.url = new String(split[0]);
        if (url.contains(HASH_TAG)) {
            hashParameters += HASH_TAG + split[1];
        }
    }

    public void appendParameter(final String key, final UrlValue value) {
        if (!alreadyContains(url, key)) {
            appendSeparator(url, PARAM_TAG);
            appendParameter(url, key, value.toString());
        }
    }

    public void appendParameters(final Map<String, String[]> parameters) {
        final Iterator<Entry<String, String[]>> iterator = parameters.entrySet().iterator();
        while (iterator.hasNext()) {
            final Entry<String, String[]> next = iterator.next();
            appendParameter(next.getKey(),
                    new UrlValue(next.getValue()));
        }
    }

    private boolean alreadyContains(final String buffer, final String key) {
        return buffer.indexOf(key) > 0;

    }

    private void appendParameter(String buffer, final String key, final String value) {
        buffer += key + "=" + value;
    }

    public String build() {
        url += hashParameters.toString();
        return url;
    }

    private void appendSeparator(String buffer, final String initialSeparator) {
        if (buffer.indexOf(initialSeparator) != -1) {
            buffer += "&";
        } else {
            buffer += initialSeparator;
        }
    }
}
