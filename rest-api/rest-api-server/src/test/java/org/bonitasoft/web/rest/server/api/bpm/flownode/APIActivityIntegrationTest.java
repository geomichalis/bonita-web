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
        TestHumanTask activity = createActivityWithVariables();
        
        Map<String, String> attributes = new HashMap<String, String>();
        String json = "[" +
                    "{\"name\": \"variable1\", \"value\": \"newValue\"}" +
        		"]";
        attributes.put("variables", json);
        
        apiActivity.runUpdate(makeAPIID(activity.getId()), attributes);
        
        assertThat(activity.getDataInstance("variable1").getValue(), is((Serializable) "newValue"));
    }

    /**
     * Variables :
     *  - variable1 : String
     *  - variable2 : Long
     */
    private TestHumanTask createActivityWithVariables() throws InvalidExpressionException {
        ProcessDefinitionBuilder processDefinitionBuidler = new ProcessDefinitionBuilder().createNewInstance("processName", "1.0");
        processDefinitionBuidler.addActor("Employees", true)
                .addDescription("This a default process")
                .addStartEvent("Start")
                .addUserTask("Activity 1", "Employees")
                
                .addData("variable1", String.class.getName(), new ExpressionBuilder().createConstantStringExpression("defaultValue"))
                .addData("variable2", Long.class.getName(), new ExpressionBuilder().createConstantLongExpression(1))
                
                .addEndEvent("Finish");
        return new TestProcess(processDefinitionBuidler).addActor(getInitiator()).setEnable(true).startCase().getNextHumanTask();
    }

}
