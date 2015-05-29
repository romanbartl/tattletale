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
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;

import org.jboss.tattletale.reporting.KeyFilter;
import org.jboss.tattletale.reporting.abstracts.ClassLocationReportAbstract;
import org.jboss.tattletale.reporting.common.ReportSeverity;
import org.jboss.tattletale.reporting.common.ReportStatus;
import org.jboss.tattletale.reporting.interfaces.Filter;
/**
 * Class location report
 *
 * @author Jesper Pedersen <jesper.pedersen@jboss.org>
 * @author <a href="mailto:torben.jaeger@jit-consulting.de">Torben Jaeger</a>
 */
public class ClassLocationReport extends ClassLocationReportAbstract
{
	
	public void writeJSON(BufferedWriter bw) throws IOException
	{
		bw.write("{\"main\":\"..index.json\",");
		bw.write("{\"classes\":[");
		
	    for (Map.Entry<String, SortedSet<String>> entry : gProvides.entrySet())
	    {
	       String clz = (String) ((Map.Entry) entry).getKey();
	       SortedSet archives = (SortedSet) ((Map.Entry) entry).getValue();
	       boolean filtered = isFiltered(clz);

	       if (!filtered)
	       {
	          if (archives.size() > 1)
	          {
	             status = ReportStatus.YELLOW;
	          }
	       }

           bw.write("\"class\":\"" + clz + "\",");
           bw.write("\"jar_files\":[");

	       Iterator sit = archives.iterator();
	       while (sit.hasNext())
	       {
	    	  bw.write("{");

	          String archive = (String) sit.next();
	          int finalDot = archive.lastIndexOf(".");
	          String extension = archive.substring(finalDot + 1);
	          
	          bw.write("\"archive\":\"" + archive + "\",");
	          bw.write("\"link\":\"../" + extension + "/" + archive + ".html + \",");
	          
	          
	          bw.write("}");
	          if (sit.hasNext()) bw.write(", ");
	       }

	       bw.write("]");

	    }		
		
		bw.write("]}");
	}
}
