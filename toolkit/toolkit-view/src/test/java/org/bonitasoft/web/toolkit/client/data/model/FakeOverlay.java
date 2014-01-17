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
package org.bonitasoft.web.toolkit.client.data.model;

import java.util.Map;

/**
 * @author Vincent Elcrin
 */
public class FakeOverlay implements Overlay {

    private final Map<String, Object> map;

    public FakeOverlay(Map<String, Object> map) {
        this.map = map;
    }

    @Override
    public String get(String key) {
        return (String) map.get(key);
    }

    @Override
    public <J extends Overlay> J getNestedOverlay(String key) {
        return (J) map.get(key);
    }

    @Override
    public <O extends Object> void set(String key, O value) {
        map.put(key, value);
    }

    @Override
    public String[] keys() {
        return map.keySet().toArray(new String[0]);
    }
}
