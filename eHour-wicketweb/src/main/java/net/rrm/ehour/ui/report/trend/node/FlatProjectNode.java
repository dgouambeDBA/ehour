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

package net.rrm.ehour.ui.report.trend.node;

import java.io.Serializable;

import net.rrm.ehour.report.reports.element.FlatReportElement;
import net.rrm.ehour.report.reports.element.ReportElement;
import net.rrm.ehour.ui.report.node.ReportNode;

/**
 * Flat project report node 
 **/

public class FlatProjectNode extends ReportNode
{
	private static final long serialVersionUID = -9117864025503755613L;

	/**
	 * 
	 * @param element
	 * @param hierarchyLevel
	 */
	public FlatProjectNode(FlatReportElement element, int hierarchyLevel)
    {
		this.id = element.getProjectId();
		this.columnValues = new String[]{element.getProjectName()};
		this.hierarchyLevel = hierarchyLevel;
    }

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.persistence.persistence.ui.report.node.ReportNode#getElementId(net.rrm.ehour.persistence.persistence.report.reports.element.ReportElement)
	 */
	@Override
	protected Serializable getElementId(ReportElement element)
	{
		return ((FlatReportElement)element).getProjectId();
	}

}
