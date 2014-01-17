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

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.web.toolkit.client.data.item.DummyItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Vincent Elcrin
 */
@RunWith(MockitoJUnitRunner.class)
public class OverlayModelTest {

    Map<String, Object> hook = new HashMap<String, Object>();

    Overlay overlay = new FakeOverlay(hook);

    Overlay nested = new FakeOverlay(new HashMap<String, Object>());

    OverlayModel model = new OverlayModel(overlay, new DummyItemDefinition());

    @Before
    public void setUp() throws Exception {
        overlay.set("foo", "bar");
        overlay.set("bar", nested);
        nested.set("baz", "qux");
    }

    @Test
    public void getValue_should_return_overlay_value_as_string_when_exist() throws Exception {
        assertEquals("bar", model.getValue("foo"));
    }

    @Test
    public void getValue_should_return_nested_overlay_value_as_string_when_exist() throws Exception {
        assertEquals("qux", model.getValue("bar.baz"));
    }

    @Test
    public void getValue_should_return_null_when_property_is_not_found() throws Exception {
        assertEquals(null, model.getValue("baz"));
    }

    @Test
    public void getValue_should_return_null_when_nested_value_is_not_found() throws Exception {
        assertEquals(null, model.getValue("bar.qux"));
    }

    @Test
    public void getValue_should_return_null_when_nested_name_end_with_a_dot() throws Exception {
        assertEquals(null, model.getValue("foo."));
    }

    @Test
    public void getValue_should_return_null_when_nested_name_start_with_a_dot() throws Exception {
        assertEquals(null, model.getValue(".foo"));
    }

    @Test
    public void setValue_should_set_overlay_value() throws Exception {
        model.setValue("foo", "bar");

        assertEquals("bar", hook.get("foo"));
    }

    @Test
    public void getDeploy_should_return_deployed_item_when_exist() throws Exception {
        IItem item = model.getDeploy("bar");

        assertEquals("qux", item.getAttributeValue("baz"));
    }

    @Test
    public void keys_should_get_overlay_keys() throws Exception {
        assertEquals("[foo, bar]", model.keys().toString());
    }

    @Test
    public void copy_should_copy_all_map_entries() throws Exception {
        HashMap<String, String> origin = new HashMap<String, String>();
        origin.put("key1", "value1");
        origin.put("key2", "value2");
        HashMap<String, Object> hook = new HashMap<String, Object>();
        Model model = new OverlayModel(new FakeOverlay(hook), new DummyItemDefinition());

        model.copy(origin);

        assertEquals(origin.toString(), hook.toString());
    }
}
