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
package org.bonitasoft.web.rest.server.datastore.utils;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.io.Serializable;

import org.bonitasoft.web.rest.server.framework.json.JacksonDeserializer;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;

public class VariableMapper {

    private JacksonDeserializer jacksonDeserializer = new JacksonDeserializer();
    private Variable variable;

    public VariableMapper(Variable variable) {
        this.variable = variable;
    }
    
    public Serializable getSerializableValue(String className) {
        try {
            return (Serializable) jacksonDeserializer.convertValue(variable.getValue(), Class.forName(className));
        } catch (ClassNotFoundException e) {
            throw new APIException(_("%className% not found. Only jdk types are supported", new Arg("className", className)));
        } catch (ClassCastException e) {
            throw new APIException(_("%className% is not Serializable", new Arg("className", className)));
        }
    }
    
    public String getName() {
        return variable.getName();
    }
}
