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
package org.bonitasoft.web.rest.server.engineclient;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.process.*;
import org.bonitasoft.engine.exception.SearchException;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.web.rest.model.bpm.cases.ArchivedCaseDefinition;
import org.bonitasoft.web.rest.model.bpm.cases.CaseDefinition;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIItemNotFoundException;
import org.bonitasoft.web.toolkit.client.common.i18n._;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.data.APIID;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author Colin PUY
 * @author Elias Ricken de Medeiros
 * 
 */
// TODO migrate all engine methods relating to cases (i.e. especially those in CaseDatastore) in this class
public class CaseEngineClient {

    protected ProcessAPI processAPI;

    public CaseEngineClient(ProcessAPI processAPI) {
        this.processAPI = processAPI;
    }
    
    public ProcessInstance start(long processId) {
        return start(processId, null);
    }
    
    public ProcessInstance start(long processId, Map<String, Serializable> variables) {
        try {
            if (variables == null || variables.isEmpty()) {
                return processAPI.startProcess(processId);
            } else {
                return processAPI.startProcess(processId, variables);
            }
        } catch (ProcessDefinitionNotFoundException e) {
            throw new APIException(new _("Can't start process, process %processId% not found", new Arg("processId", processId)), e);
        } catch (ProcessActivationException e) {
            throw new APIException(new _("Can't start process, process %processId% is not enabled", new Arg("processId", processId)), e);
        } catch (ProcessExecutionException e) {
            throw new APIException(new _("Error occured when starting process %processId%", new Arg("processId", processId)), e);
        }
    }
    
    public long countOpenedCases() {
        SearchOptions search = new SearchOptionsBuilder(0, 0).done();
        try {
            return processAPI.searchOpenProcessInstances(search).getCount();
        } catch (Exception e) {
            throw new APIException("Error when counting opened cases", e);
        }
    }
    
    public List<ArchivedProcessInstance> searchArchivedCasesInAllStates(SearchOptions searchOptions) {
        try {
            return processAPI.searchArchivedProcessInstancesInAllStates(searchOptions).getResult();
        } catch (SearchException e) {
            throw new APIException("Error when searching cases in all state", e);
        }
    }

    public ProcessInstance getProcessInstance(final long processInstanceId) {
        try {
            return processAPI.getProcessInstance(processInstanceId);
        } catch (final ProcessInstanceNotFoundException e) {
            throw new APIItemNotFoundException(CaseDefinition.TOKEN, APIID.makeAPIID(processInstanceId));
        }
    }

    public ArchivedProcessInstance getArchivedProcessInstance(final long processInstanceId) {
        try {
            return processAPI.getArchivedProcessInstance(processInstanceId);
        } catch (final ArchivedProcessInstanceNotFoundException e) {
            throw new APIItemNotFoundException(ArchivedCaseDefinition.TOKEN, APIID.makeAPIID(processInstanceId));
        }
    }
}
