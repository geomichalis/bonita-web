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
package org.bonitasoft.console.client.common.view.uiBinded;

import org.bonitasoft.web.rest.model.bpm.flownode.HumanTaskItem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;

/**
 * @author Vincent Elcrin
 * 
 */
public class FormsUrlBuilder {

    private final String UUID_SEPERATOR = "--";

    public String build(HumanTaskItem task) {
        return buildTasksFormURL(task);
    }

    private String buildTasksFormURL(final HumanTaskItem task) {
        return new StringBuilder()
                .append(GWT.getModuleBaseURL()).append("homepage")
                .append("?ui=form")
                .append("#form=").append(buildFormToken(task))
                .append("$entry")
                .append("&task=").append(task.getId())
                .append("&mode=form")
                .toString();
    }

    private String buildFormToken(final HumanTaskItem task) {
        return new StringBuilder()
                .append(URL.decodeQueryString(task.getProcess().getName()))
                .append(this.UUID_SEPERATOR)
                .append(task.getProcess().getVersion()).append(this.UUID_SEPERATOR)
                .append(URL.decodeQueryString(task.getName()))
                .toString();
    }
}
