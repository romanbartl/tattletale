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
package org.jboss.tattletale.reporting.JSON;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.jboss.tattletale.core.Archive;
import org.jboss.tattletale.core.NestableArchive;
import org.jboss.tattletale.reporting.abstracts.DependantsReportAbstract;
import org.jboss.tattletale.reporting.common.ReportSeverity;

/**
 * Dependants report
 *
 * @author Jesper Pedersen <jesper.pedersen@jboss.org>
 * @author <a href="mailto:torben.jaeger@jit-consulting.de">Torben Jaeger</a>
 */
public class DependantsReport extends DependantsReportAbstract
{
	
	public void writeJSON(BufferedWriter bw) throws IOException
	{
		bw.write("{\"main\":\"../index.json\",");
		bw.write("\"archives\":[");
		
	    for (Archive archive : archives)
	    {
	       String archiveName = archive.getName();
	       int finalDot = archiveName.lastIndexOf(".");
	       String extension = archiveName.substring(finalDot + 1);

	       bw.write("\"archive\":\"" + archiveName + "\",");
	       bw.write("\"archive_link\":\"" + "../" + extension + "/" + archiveName + ".html" + "\",");


	       SortedSet<String> result = new TreeSet<String>();

	       for (Archive a : archives)
	       {

	          for (String require : getRequires(archive))
	          {

	             if (archive.doesProvide(require) && (getCLS() == null || getCLS().isVisible(a, archive)))
	             {
	                result.add(a.getName());
	             }
	          }
	       }

	       Iterator<String> resultIt = result.iterator();
	       
	       bw.write("\"dependencies\":[");
	       
	       while (resultIt.hasNext())
	       {
	    	   bw.write("{");
	    	   
	           String r = resultIt.next();
	           
	           bw.write("\"name\":" + r + "\",");
	           bw.write("\"link\":\"" + "../jar/" + r + ".html" + "\"");
	           
	           /*
	           if (r.endsWith(".jar"))
	           {
	        	  
	           }
	           else
	           {
	               bw.write("<i>" + r + "</i>");
	           }
	           */
	           
	           bw.write("}");

	           if (resultIt.hasNext()) bw.write(", ");
	       }
	       
	       bw.write("]");
	    }
	       
	    bw.write("]}");
	}

}
