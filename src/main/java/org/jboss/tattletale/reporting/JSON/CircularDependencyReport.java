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

import org.jboss.tattletale.core.Archive;
import org.jboss.tattletale.core.ArchiveTypes;
import org.jboss.tattletale.core.NestableArchive;
import org.jboss.tattletale.reporting.KeyFilter;
import org.jboss.tattletale.reporting.abstracts.CircularDependencyReportAbstract;
import org.jboss.tattletale.reporting.common.CLSReport;
import org.jboss.tattletale.reporting.common.ReportSeverity;
import org.jboss.tattletale.reporting.common.ReportStatus;
import org.jboss.tattletale.reporting.interfaces.Filter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.jboss.tattletale.reporting.common.*;
import org.jboss.tattletale.reporting.interfaces.*;
/**
 * Circular dependency report
 *
 * @author Jesper Pedersen <jesper.pedersen@jboss.org>
 */
public class CircularDependencyReport extends CircularDependencyReportAbstract
{
   /**
    * write out the report's content
    *
    * @param bw the writer to use
    * @throws IOException if an error occurs
    */
   public void writeHtmlBodyContent(BufferedWriter bw) throws IOException
   {
      bw.write("<table>" + Dump.newLine());

      bw.write("  <tr>" + Dump.newLine());
      bw.write("     <th>Archive</th>" + Dump.newLine());
      bw.write("     <th>Circular Dependencies</th>" + Dump.newLine());
      bw.write("  </tr>" + Dump.newLine());

      SortedMap<String, SortedSet<String>> dependsOnMap = recursivelyBuildDependsOnFromArchive(archives);
      SortedMap<String, SortedSet<String>> transitiveDependsOnMap = new TreeMap<String, SortedSet<String>>();
      Iterator<Map.Entry<String, SortedSet<String>>> dit = dependsOnMap.entrySet().iterator();
      while (dit.hasNext())
      {
         Map.Entry<String, SortedSet<String>> entry = dit.next();

         String archive = entry.getKey();
         SortedSet<String> value = entry.getValue();

         SortedSet<String> result = new TreeSet<String>();

         if (value != null && value.size() > 0)
         {
            for (String aValue : value)
            {
               resolveDependsOn(aValue, archive, dependsOnMap, result);
            }
         }

         transitiveDependsOnMap.put(archive, result);
      }

      boolean odd = true;

      dit = transitiveDependsOnMap.entrySet().iterator();
      while (dit.hasNext())
      {
         Map.Entry<String, SortedSet<String>> entry = dit.next();

         String archive = entry.getKey();
         SortedSet<String> value = entry.getValue();

         int finalDot = archive.lastIndexOf(".");
         String extension = archive.substring(finalDot + 1);

         if (value.size() != 0)
         {
            SortedSet<String> circular = new TreeSet<String>();

            Iterator<String> valueIt = value.iterator();
            while (valueIt.hasNext())
            {
               String r = valueIt.next();
               SortedSet<String> td = transitiveDependsOnMap.get(r);
               if (td != null && td.contains(archive))
               {
                  circular.add(r);
               }
            }

            if (circular.size() > 0)
            {
               boolean filtered = isFiltered(archive);
               if (!filtered)
               {
                  status = ReportStatus.RED;
               }

               if (odd)
               {
                  bw.write("  <tr class=\"rowodd\">" + Dump.newLine());
               }
               else
               {
                  bw.write("  <tr class=\"roweven\">" + Dump.newLine());
               }
               bw.write("     <td><a href=\"../" + extension + "/" + archive + ".html\">" + archive + "</a></td>" +
                     Dump.newLine());
               if (!filtered)
               {
                  bw.write("     <td>");
               }
               else
               {
                  bw.write("     <td style=\"text-decoration: line-through;\">");
               }

               valueIt = value.iterator();
               while (valueIt.hasNext())
               {
                  String r = valueIt.next();

                  if (circular.contains(r))
                  {
                     bw.write("<a href=\"../" + extension + "/" + r + ".html\">" + r + " (*)</a>");
                  }
                  else
                  {
                     bw.write("<a href=\"../" + extension + "/" + r + ".html\">" + r + "</a>");
                  }

                  if (valueIt.hasNext())
                  {
                     bw.write(", ");
                  }
               }

               bw.write("</td>" + Dump.newLine());
               bw.write("  </tr>" + Dump.newLine());

               odd = !odd;
            }
         }
      }

      bw.write("</table>" + Dump.newLine());
   }

   /**
    * write out the header of the report's content
    *
    * @param bw the writer to use
    * @throws IOException if an errror occurs
    */
   public void writeHtmlBodyHeader(BufferedWriter bw) throws IOException
   {
      bw.write("<body>" + Dump.newLine());
      bw.write(Dump.newLine());

      bw.write("<h1>" + NAME + "</h1>" + Dump.newLine());

      bw.write("<a href=\"../index.html\">Main</a>" + Dump.newLine());
      bw.write("<p>" + Dump.newLine());
   }

}
