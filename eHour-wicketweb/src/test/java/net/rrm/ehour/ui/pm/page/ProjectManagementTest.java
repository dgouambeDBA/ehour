/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.ui.pm.page;

import com.google.common.collect.Lists;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.report.reports.ProjectManagerReport;
import net.rrm.ehour.report.service.AggregateReportService;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.easymock.EasyMock.*;

/**
 * @author Thies
 */
public class ProjectManagementTest extends BaseSpringWebAppTester {
    ProjectService projectService;

    AggregateReportService aggregateReportService;

    @Before
    public void before() throws Exception {
        projectService = createMock(ProjectService.class);
        getMockContext().putBean("projectService", projectService);

        aggregateReportService = createMock(AggregateReportService.class);
        getMockContext().putBean("aggregatedReportService", aggregateReportService);
    }

    @Test
    public void shouldRender() throws SecurityException, NoSuchMethodException {
        List<Project> projects = Lists.newArrayList();
        projects.add(new Project(1));

        ProjectManagerReport report = new ProjectManagerReport();
        report.setProject(new Project(1));
        report.setReportRange(new DateRange(new Date(), new Date()));
        expect(aggregateReportService.getProjectManagerDetailedReport(isA(DateRange.class), isA(Integer.class)))
                .andReturn(report)
                .anyTimes();

        expect(projectService.getProjectManagerProjects(isA(User.class)))
                .andReturn(projects);
//		getTester().createRequestCycle();

        replay(projectService);
        replay(aggregateReportService);
//		getTester().setupRequestAndResponse(true);
        getTester().startPage(ProjectManagement.class);
        getTester().assertRenderedPage(ProjectManagement.class);
        getTester().assertNoErrorMessage();


        FormTester form = getTester().newFormTester("sidePanel:criteriaForm");
        form.select("userSelectedCriteria.projects", 0);

        form.submit("submitButton");

        getTester().executeAjaxEvent("sidePanel:criteriaForm:submitButton", "onclick");

        getTester().assertNoErrorMessage();
        verify(projectService);

        verify(aggregateReportService);

        getTester().assertComponent("reportPanel", AjaxTabbedPanel.class);
        getTester().assertComponentOnAjaxResponse("reportPanel");
    }
}
