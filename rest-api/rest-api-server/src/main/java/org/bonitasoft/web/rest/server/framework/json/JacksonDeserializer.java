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

import java.io.IOException;
import java.util.List;

import org.bonitasoft.web.rest.server.datastore.bpm.flownode.Variable;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class JacksonDeserializer {

    private ObjectMapper mapper = new ObjectMapper();

    public <T> T deserialize(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json.getBytes(), clazz);
        } catch (JsonParseException e) {
            throw new APIException(_("Can't parse json, non-well formed content"), e);
        } catch (JsonMappingException e) {
            throw new APIException(_("Json can't be mapped to " + clazz.getName()), e);
        } catch (IOException e) {
            // should never appear
            throw new APIException(e);
        }
    }
    
    public <T> List<T> deserializeList(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json.getBytes() , mapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (Exception e) {
            throw new APIException(e);
        }
    }
}
