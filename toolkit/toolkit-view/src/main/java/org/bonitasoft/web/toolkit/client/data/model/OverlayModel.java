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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * @author Vincent Elcrin
 */
public class OverlayModel implements Model {

    private Overlay overlay;

    private ItemDefinition<?> definition;

    public  OverlayModel(Overlay overlay, ItemDefinition<?> definition) {
        this.overlay = overlay;
        this.definition = definition;
    }

    @Override
    public String getValue(String name) {
        int index = name.indexOf('.');
        if(index < 0 || isInvalid(name, index)) {
            return overlay.get(name);
        }
        OverlayModel deploy = new OverlayModel(overlay.getNestedOverlay(name.substring(0, index)), definition);
        return deploy.getValue(name.substring(index + 1));
    }

    @Override
    public IItem getDeploy(String name) {
        ItemDefinition<?> definition = this.definition.getDeployDefinition(name);
        return definition.createItem(new OverlayModel(overlay.getNestedOverlay(name), definition));
    }

    private boolean isInvalid(String name, int index) {
        return index == 0 || index == name.length() - 1;
    }

    @Override
    public void setValue(String name, String value) {
        overlay.set(name, value);
    }

    @Override
    public Set<String> keys() {
        return new HashSet<String>(Arrays.asList(overlay.keys()));
    }

    @Override
    public void copy(Map<String, String> attributes) {
        for (Map.Entry<String, String> attribute : attributes.entrySet()) {
            overlay.set(attribute.getKey(), attribute.getValue());
        }
    }
}
