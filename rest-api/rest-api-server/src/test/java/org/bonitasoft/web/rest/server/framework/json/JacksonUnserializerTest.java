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

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.Serializable;
import java.util.Date;

import org.bonitasoft.web.rest.server.APITestWithMock;
import org.bonitasoft.web.rest.server.framework.json.model.Address;
import org.bonitasoft.web.rest.server.framework.json.model.User;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.junit.Before;
import org.junit.Test;

/** extends APITestWithMock to avoid nullpointerException on I18n */
public class JacksonUnserializerTest extends APITestWithMock {

    private JacksonUnserializer jacksonUnserializer;

    @Before
    public void intializeUnserializer() {
        jacksonUnserializer = new JacksonUnserializer();
    }
    
    @Test(expected = APIException.class)
    public void unserialize_throw_exception_if_class_is_not_in_classpath() throws Exception {
        jacksonUnserializer.unserialize("someJson", "not.in.classpath.Object");
    }
    
    @Test(expected = APIException.class)
    public void unserialize_throw_exception_if_class_is_not_serializable() throws Exception {
        jacksonUnserializer.unserialize("someJson", this.getClass().getName());
    }
    
    @Test
    public void unserialize_can_unserialize_primitives_types() throws Exception {
        
        Serializable serializable = jacksonUnserializer.unserialize("1", Long.class.getName());
        
        assertThat(serializable, is((Serializable) 1L));
    }
    
    @Test
    public void unserialize_can_unserialize_complex_types() throws Exception {
        User expectedUser = new User(1, "Colin", "Puy", new Date(428558400000L), new Address("310 La Gouterie", "Charnècles"));
        String json = "{\"address\":{\"street\":\"310 La Gouterie\",\"city\":\"Charnècles\"},\"id\":1,\"firstName\":\"Colin\",\"lastName\":\"Puy\",\"birthday\":428558400000}";
        
        Serializable serializable = jacksonUnserializer.unserialize(json, User.class.getName());
        
        assertThat(serializable, is((Serializable) expectedUser));
    }
}
