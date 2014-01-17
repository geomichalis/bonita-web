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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bonitasoft.web.toolkit.client.data.item.IItem;

/**
 * @author Vincent Elcrin
 */
public class MapModel implements Model {

    private final Map<String, String> attributes;

    private Map<String, IItem> deploys;

    public MapModel(Map<String, String> attributes, Map<String, IItem> deploys) {
        this.attributes = attributes;
        this.deploys = deploys;
    }

    public MapModel(Map<String, String> attributes) {
        this(attributes, new HashMap<String, IItem>());
    }

    @Override
    public String getValue(String name) {
        return attributes.get(name);
    }

    @Override
    public IItem getDeploy(String name) {
        return deploys.get(name);
    }

    public void addDeploy(String key, IItem item) {
        deploys.put(key, item);
    }

    @Override
    public void setValue(String name, String value) {
        attributes.put(name, value);
    }

    @Override
    public Set<String> keys() {
        Set<String> keys = new HashSet<String>(attributes.keySet());
        keys.addAll(deploys.keySet());
        return keys;
    }

    @Override
    public void copy(Map<String, String> attributes) {
        this.attributes.putAll(attributes);
    }

}
