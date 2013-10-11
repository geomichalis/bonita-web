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

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.web.rest.server.framework.json.JacksonDeserializer;
import org.bonitasoft.web.toolkit.client.common.util.StringUtil;

public class VariablesMapper {
    
    private static JacksonDeserializer jacksonDeserializer = new JacksonDeserializer();
    private List<VariableMapper> variables;
    
    private VariablesMapper(List<VariableMapper> variables) {
        this.variables = variables;
    }
    
    public static VariablesMapper fromJson(String json) {
        List<Variable> list = jacksonDeserializer.deserializeList(json, Variable.class);
        ArrayList<VariableMapper> list2 = new ArrayList<VariableMapper>();
        for (Variable variable : list) {
            if (!StringUtil.isBlank(variable.getName())) {
                list2.add(new VariableMapper(variable));
            }
        }
        return new VariablesMapper(list2);
    }

    public List<VariableMapper> getVariables() {
        return variables;
    }

}
