/*
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

import org.bonitasoft.console.client.admin.bpm.cases.view.CaseListingAdminPage;
import org.bonitasoft.console.client.admin.bpm.task.view.TaskListingAdminPage;
import org.bonitasoft.console.client.admin.process.view.ProcessListingAdminPage;
import org.bonitasoft.console.client.user.application.view.ProcessListingPage;
import org.bonitasoft.console.client.user.cases.view.CaseListingPage;
import org.bonitasoft.console.client.user.task.model.TaskAPI;
import org.bonitasoft.console.client.user.task.view.TasksListingPage;
import org.bonitasoft.web.rest.model.bpm.flownode.HumanTaskDefinition;
import org.bonitasoft.web.rest.model.bpm.flownode.HumanTaskItem;
import org.bonitasoft.web.toolkit.client.Session;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.page.ItemNotFoundPopup;
import org.bonitasoft.web.toolkit.client.ui.page.PageOnItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PerformTaskPage extends PageOnItem<HumanTaskItem> {

    public static final List<String> PRIVILEGES = new ArrayList<String>();

    static {
        PRIVILEGES.add(TasksListingPage.TOKEN);
        PRIVILEGES.add(TaskListingAdminPage.TOKEN); // FIX ME: we should create a humantaskmoredetails admin page so ill never need this
        PRIVILEGES.add(CaseListingPage.TOKEN);
        PRIVILEGES.add(CaseListingAdminPage.TOKEN);
        PRIVILEGES.add(ProcessListingPage.TOKEN);
        PRIVILEGES.add(ProcessListingAdminPage.TOKEN);
        PRIVILEGES.add("reportlistingadminext");
    }

    public final static String TOKEN = "performTask";

    public PerformTaskPage() {
        super(Definitions.get(HumanTaskDefinition.TOKEN));
    }

    public PerformTaskPage(final APIID taskId) {
        this();
        addParameter(PARAMETER_ITEM_ID, taskId.toString());
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    public void defineTitle(final HumanTaskItem item) {
        this.setTitle(item.getDisplayName());
    }

    @Override
    public void buildView(final HumanTaskItem task) {
        if (isAssignedToMe(task)) {
            addBody(new UiPerformTaskComponent(task));
        } else if (isUnassigned(task)) {
            claimTask(task, new UiPerformTaskComponent(task));
        } else {
            // addBody(new Paragraph(_("You can't perform this task, it has already been assigned to someone else.")));
        }
    }

    /**
     * @param task
     * @return
     */
    private boolean isAssignedToMe(HumanTaskItem task) {
        return task.getAssignedId() != null && task.getAssignedId().equals(getUserId());
    }

    private void claimTask(final HumanTaskItem task, final UiPerformTaskComponent component) {
        TaskAPI.claim(task.getId(), getUserId(), new Action() {

            @Override
            public void execute() {
                addBody(component);
            }
        });
    }

    private boolean isUnassigned(final HumanTaskItem task) {
        return task.getAssignedId() == null;
    }

    /**
     * @return
     */
    private APIID getUserId() {
        return Session.getUserId();
    }

    @Override
    protected void onItemNotFound() {
        ViewController.showPopup(new ItemNotFoundPopup(TasksListingPage.TOKEN));
    }

    @Override
    protected List<String> defineDeploys() {
        return Arrays.asList(HumanTaskItem.ATTRIBUTE_PROCESS_ID);
    }
}
