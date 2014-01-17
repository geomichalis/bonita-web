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

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Vincent Elcrin
 */
@RunWith(MockitoJUnitRunner.class)
public class MapModelTest {

    Map<String, String> values = new HashMap<String, String>();

    Map<String, IItem> deploys = new HashMap<String, IItem>();

    Model model = new MapModel(values, deploys);

    @Mock
    IItem item;

    @Before
    public void setUp() throws Exception {
        values.put("foo", "bar");
        when(item.getAttributeValue("name")).thenReturn("qux");
        deploys.put("baz", item);
    }

    @Test
    public void getValue_should_return_value_when_property_exist() throws Exception {
        assertEquals("bar", model.getValue("foo"));
    }

    @Test
    public void getValue_should_return_null_when_property_doesnt_exist() throws Exception {
        assertEquals(null, model.getValue("bar"));
    }

    @Test
    public void getDeploy_should_return_item_when_property_exist() throws Exception {
        assertEquals("qux", model.getDeploy("baz").getAttributeValue("name"));
    }

    @Test
    public void getDeploy_should_return_null_when_property_exist() throws Exception {
        assertEquals(null, model.getDeploy("qux"));
    }

    @Test
    public void keys_should_return_all_model_keys() throws Exception {
        assertEquals("[baz, foo]", model.keys().toString());
    }

    @Test
    public void a_set_value_should_be_retrievable() throws Exception {
        Map<String, String> values = new HashMap<String, String>();
        Model model = new MapModel(values);

        model.setValue("baz", "qux");

        assertEquals("qux", model.getValue("baz"));
    }

    @Test
    public void a_set_deploy_should_be_retrievable() throws Exception {
        Map<String, IItem> deploys = new HashMap<String, IItem>();
        MapModel model = new MapModel(new HashMap<String, String>(), deploys);
        when(item.getAttributeValue("name")).thenReturn("baz");

        model.addDeploy("qux", item);

        assertEquals("baz", model.getDeploy("qux").getAttributeValue("name"));
    }

    @Test
    public void all_copied_values_should_be_retrievable() throws Exception {
        Map<String, String> origin = new HashMap<String, String>();
        origin.put("foo", "bar");
        origin.put("baz", "qux");
        Map<String, String> hook = new HashMap<String, String>();
        Model model = new MapModel(hook);

        model.copy(origin);

        assertEquals(origin.toString(), hook.toString());
    }
}
