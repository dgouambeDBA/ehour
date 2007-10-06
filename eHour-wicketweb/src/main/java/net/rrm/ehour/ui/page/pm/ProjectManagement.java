/**
 * Created on Oct 6, 2007
 * Created by Thies Edeling
 * Created by Thies Edeling
 * Copyright (C) 2007 TE-CON, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.page.pm;

import java.util.List;

import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.report.criteria.AvailableCriteria;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.ui.page.BasePage;
import net.rrm.ehour.ui.panel.contexthelp.ContextualHelpPanel;
import net.rrm.ehour.ui.panel.report.user.criteria.UserReportCriteriaPanel;
import net.rrm.ehour.ui.session.EhourWebSession;
import net.rrm.ehour.user.domain.User;

import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Project management base station :)
 **/
@AuthorizeInstantiation("ROLE_PROJECTMANAGER")
public class ProjectManagement extends BasePage
{
	private static final long serialVersionUID = 898442184509251553L;

	@SpringBean
	private ProjectService	projectService;
	
	/**
	 * Default constructor 
	 * @param pageTitle
	 * @param model
	 */
	public ProjectManagement()
	{
		super(new ResourceModel("pmReport.title"), null);
		
		ReportCriteria reportCriteria = getReportCriteria();
		
		IModel	model = new CompoundPropertyModel(reportCriteria);
		setModel(model);
		
		// contextual help
		add(new ContextualHelpPanel("contextHelp"));

		// add criteria
		add(new UserReportCriteriaPanel("sidePanel", model));
	}

	/**
	 * Get report criteria
	 * @return
	 */
	private ReportCriteria getReportCriteria()
	{
		ReportCriteria reportCriteria = new ReportCriteria();
		
		User user = ((EhourWebSession)getSession()).getUser().getUser();
		
		List<Project> projects = projectService.getProjectManagerProjects(user);
		
		AvailableCriteria availCriteria = reportCriteria.getAvailableCriteria();
		availCriteria.setProjects(projects);
		
		return reportCriteria;
	}
}
