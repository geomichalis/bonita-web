/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * 
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
package org.bonitasoft.web.rest.server.framework.json;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.io.Serializable;

import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.codehaus.jackson.map.ObjectMapper;

public class JacksonUnserializer {

    private ObjectMapper mapper = new ObjectMapper();

    public Serializable unserialize(String json, String className) {
        Class<?> clazz = getClass(className);
        return unserialize(json, clazz);
    }

    private Class<?> getClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new APIException(_("%className% not found. Only jdk types are supported", new Arg("className", className)));
        }
    }

    private Serializable unserialize(String json, Class<?> clazz) {
        try {
            return (Serializable) mapper.readValue(json.getBytes(), clazz);
        } catch (ClassCastException e) {
            throw new APIException(_("%className% is not serializable", new Arg("className", clazz.getName())));
        } catch (Exception e) {
            throw new APIException(_("Unable to unserialize %json% in %className%", new Arg("json", json),
                    new Arg("className", clazz.getName())), e);
        }
    }
}
