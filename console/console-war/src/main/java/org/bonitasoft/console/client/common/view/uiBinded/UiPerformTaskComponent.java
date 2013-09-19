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

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Element;
import org.bonitasoft.web.rest.model.bpm.flownode.HumanTaskItem;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.ui.component.IFrame;
import org.bonitasoft.web.toolkit.client.ui.component.button.ButtonAction;
import org.bonitasoft.web.toolkit.client.ui.component.core.Component;
import org.bonitasoft.web.toolkit.client.ui.component.event.ActionEvent;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

public class UiPerformTaskComponent extends Component {

    @UiField(provided = true)
    public static final String BACK_BTN_LABEL = _("Back");

    @UiField(provided = true)
    public static final String BACK_BTN_TOOLTIP = _("Click to go back");

    // Potential XSS vulnerability (Use carefully!)
    @UiField(provided = true)
    public static final SafeHtml PAGE_TITLE = SafeHtmlUtils.fromTrustedString(
            _("The title: %1%Perform your process%2%",
                    new Arg("1", "<span id=\"task-title\">"),
                    new Arg("2", "</span>")));

    // XSS vulnerability free! (but no internationalization)
    // interface Templates extends SafeHtmlTemplates {
    //
    // @SafeHtmlTemplates.Template("The title: <span id=\"bold\">Perform your process</span>")
    // SafeHtml PAGE_TITLE();
    //
    // }
    // private static final Templates TEMPLATES = GWT.create(Templates.class);

    private HumanTaskItem task;

    @UiField(provided = true)
    IFrame iFrame;

    @UiField
    ButtonAction backButton;

    interface ObjectUiBinder extends UiBinder<HtmlMarker<DivElement>, UiPerformTaskComponent> {
    }

    private static ObjectUiBinder ui = GWT.create(ObjectUiBinder.class);

    public UiPerformTaskComponent(HumanTaskItem task) {
        this.task = task;
    }

    @Override
    protected Element makeElement() {
        iFrame = new IFrame(new FormsUrlBuilder().build(task));
        return ui.createAndBindUi(this).getElement();
    }

    @UiHandler("backButton")
    public void doBackButtonAction(ActionEvent e) {
        ViewController.back();
    }

}
