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
package org.bonitasoft.web.rest.server.api.bpm.flownode;

import static org.bonitasoft.web.toolkit.client.data.APIID.makeAPIID;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.data.DataInstance;
import org.bonitasoft.engine.bpm.data.DataNotFoundException;
import org.bonitasoft.engine.bpm.process.impl.ProcessDefinitionBuilder;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.expression.ExpressionBuilder;
import org.bonitasoft.engine.expression.InvalidExpressionException;
import org.bonitasoft.test.toolkit.bpm.TestCase;
import org.bonitasoft.test.toolkit.bpm.TestCaseFactory;
import org.bonitasoft.test.toolkit.bpm.TestHumanTask;
import org.bonitasoft.test.toolkit.bpm.TestProcess;
import org.bonitasoft.test.toolkit.bpm.TestProcessFactory;
import org.bonitasoft.test.toolkit.organization.TestUser;
import org.bonitasoft.test.toolkit.organization.TestUserFactory;
import org.bonitasoft.web.rest.server.AbstractConsoleTest;
import org.bonitasoft.web.rest.server.api.bpm.cases.APIArchivedComment;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.junit.Test;

public class APIActivityIntegrationTest extends AbstractConsoleTest {

    private APIActivity apiActivity;

    @Override
    public void consoleTestSetUp() throws Exception {
        apiActivity = new APIActivity();
        apiActivity.setCaller(getAPICaller(TestUserFactory.getJohnCarpenter().getSession(), "API/bpm/activity"));
    }

    @Override
    protected TestUser getInitiator() {
        return TestUserFactory.getJohnCarpenter();
    }   
    
    @Test
    public void api_can_update_activity_variables() throws Exception {
        TestProcess process = new TestProcess(getProcess());
        TestCase case1 = process.addActor(getInitiator()).setEnable(true).startCase();
        
        TestHumanTask nextHumanTask = case1.getNextHumanTask();
        
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put("variables", "[{\"name\": \"variable1\", \"value\": \"newValue\"}]");
        apiActivity.runUpdate(makeAPIID(nextHumanTask.getId()), attributes);
        
        assertThat(getDataInstance(nextHumanTask).getValue(), is((Serializable) "newValue"));
    }

    private DataInstance getDataInstance(TestHumanTask nextHumanTask) throws Exception{
        return TenantAPIAccessor.getProcessAPI(getInitiator().getSession()).getActivityDataInstance("variable1", nextHumanTask.getId());
    }

    private ProcessDefinitionBuilder getProcess() throws InvalidExpressionException {
        ProcessDefinitionBuilder processDefinitionBuidler = new ProcessDefinitionBuilder().createNewInstance("processName", "1.0");
        processDefinitionBuidler.addActor("Employees", true)
                .addDescription("This a default process")
                .addStartEvent("Start")
                .addUserTask("Activity 1", "Employees")
                .addData("variable1", String.class.getName(), new ExpressionBuilder().createConstantStringExpression("value"))
                .addEndEvent("Finish");
        return processDefinitionBuidler;
    }

}
