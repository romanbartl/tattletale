/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.tattletale.reporting;


import org.jboss.tattletale.Version;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.SortedSet;


/**
 * JSONReport
 * This class serves to better executing of data
 * 
 * @author Roman Bártl <bbartl.roman@gmail.com>
 */

public class JSONReport 
{
	/** New line character */
	private static final String NEW_LINE = System.getProperty("line.separator");
		
	
	private static String newLine()
	{
	    return NEW_LINE;
	}	
	
	public static void generateJSON(SortedSet<Report> dependenciesReports,
			  				 		SortedSet<Report> generalReports,
			  				 		SortedSet<Report> archiveReports,
			  				 		SortedSet<Report> customReports,
			  				 		String outputDir)
	{
	      try
	      {
	          FileWriter fw = new FileWriter(outputDir + "index.json");
	          BufferedWriter bw = new BufferedWriter(fw, 8192);	
	      
		      generateJSONScript(bw, dependenciesReports, generalReports, archiveReports, customReports, outputDir);

	          bw.flush();
	          bw.close();
	      }
	      
	      catch (Exception e)
	      {
	         System.err.println("GenerateIndex: " + e.getMessage());
	         e.printStackTrace(System.err);
	      }
	
	}
	
	private static void generateJSONScript(BufferedWriter bw,
										   SortedSet<Report> dependenciesReports,
										   SortedSet<Report> generalReports,
										   SortedSet<Report> archiveReports,
										   SortedSet<Report> customReports,
										   String outputDir)
	{
		try
		{
			String scriptVar = "{";
			
			scriptVar += generateReportItems(bw, dependenciesReports, "Dependencies", false) + ", ";
			scriptVar += generateReportItems(bw, generalReports, "Reports", false) + ", ";
			scriptVar += generateReportItems(bw, archiveReports, "Archives", true);
			//scriptVar += generateReportItems(bw, customReports, "Custom reports", false);
			
			scriptVar += "}";
			
			bw.write(scriptVar);
		}
		
		catch (Exception e)
		{
			System.err.println("GenerateIndex: " + e.getMessage());
	        e.printStackTrace(System.err);			
		}
	}
	
	private static String generateReportItems(BufferedWriter bw, 
											SortedSet<Report> reports,
											String heading, 
											boolean useReportName) throws IOException
	{
		String scriptVar = "\"" + heading + "\":[";

		if (reports != null && reports.size() > 0)
		{	
			String fileBase = "index";
			
			int index = 0;
			
			for (Report r : reports)
	        {
				if (useReportName)
	            {
	               fileBase = r.getName();
	            }
				
				scriptVar += "{\"name\":\"" + r.getName() + "\", ";
				scriptVar += "\"link\":\"" + r.getDirectory() + "/" + fileBase + ".json\", ";
				scriptVar += "\"severity\":\"" + ReportSeverity.getSeverityString(r.getSeverity()) + "\", ";
				scriptVar += "\"statusColor\":\"" + ReportStatus.getStatusColor(r.getStatus()) + "\", ";
				scriptVar += "\"titulSize\":\"" + getIndexHtmlSize(r) + "\"}";
				
				if (index + 1 != reports.size()) scriptVar += ", ";
				
				index++;
	        }
			
			scriptVar += "]";
		}
		
		return scriptVar;
	}

	private static String getIndexHtmlSize(Report r)
	{
	    File indexFile = new File(r.getOutputDirectory().getAbsolutePath() + File.separator + r.getIndexName());
	    return ((indexFile.length() / 1024) + 1) + "KB";
	}
}
