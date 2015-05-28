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
package org.jboss.tattletale.reporting.abstracts;

import org.jboss.tattletale.core.Archive;
import org.jboss.tattletale.reporting.abstracts.AbstractReport;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Iterator;

import org.jboss.tattletale.reporting.common.*;
import org.jboss.tattletale.reporting.interfaces.*;
/**
 * A report that shows unused JAR archives
 *
 * @author Jesper Pedersen <jesper.pedersen@jboss.org>
 */
public abstract class UnusedJarReportAbstract extends AbstractReport
{
   /** NAME */
   private static final String NAME = "Unused Jar";

   /** DIRECTORY */
   private static final String DIRECTORY = "unusedjar";

   /** Constructor */
   public UnusedJarReportAbstract()
   {
      super(DIRECTORY, ReportSeverity.WARNING, NAME, DIRECTORY);
   }

   /**
    * Write out the report's content
    *
    * @param bw the writer to use
    * @throws IOException if an error occurs
    *
   public void writeHtmlBodyContent(BufferedWriter bw) throws IOException
   {
      bw.write("<elements>" + Dump.newLine());

    
      //bw.write("     <th>Archive</th>" + Dump.newLine());
      //bw.write("     <th>Used</th>" + Dump.newLine());
     

      boolean odd = true;
      int used = 0;
      int unused = 0;

      for (Archive archive : archives)
      {
         boolean archiveStatus = false;

         String archiveName = archive.getName();
         int finalDot = archiveName.lastIndexOf(".");
         String extension = archiveName.substring(finalDot + 1);

         Iterator<Archive> it = archives.iterator();
         while (!archiveStatus && it.hasNext())
         {
            Archive a = it.next();

            if (!archive.getName().equals(a.getName()))
            {
               Iterator<String> sit = a.getRequires().iterator();
               while (!archiveStatus && sit.hasNext())
               {
                  String require = sit.next();

                  if (archive.getProvides().keySet().contains(require))
                  {
                     archiveStatus = true;
                  }
               }
            }
         }


       
          bw.write("  <element>" + Dump.newLine());
        
         
         bw.write("     <Archive>../" + extension + "/" + archiveName +
                  ".xml" + archiveName + "</Archive>" + Dump.newLine());

         if (archiveStatus)
         {
            bw.write("     <Used>Yes</Used>" + Dump.newLine());
            used++;
         }
         else
         {
            unused++;

            if (!isFiltered(archive.getName()))
            {
               status = ReportStatus.YELLOW;
               bw.write("     <Used>No</Used>" + Dump.newLine());
            }
            else
            {
               bw.write("     <Used>No</Used>" + Dump.newLine());
            }
         }

         bw.write("  </element>" + Dump.newLine());

         odd = !odd;
      }

      bw.write("</elements>" + Dump.newLine());

      bw.write(Dump.newLine());
     

      bw.write("<elements>" + Dump.newLine());

    
      //bw.write("     <th>Status</th>" + Dump.newLine());
      //bw.write("     <th>Archives</th>" + Dump.newLine());
      

      bw.write("  <element>" + Dump.newLine());
      bw.write("     <Status>Used</Status>" + Dump.newLine());
      bw.write("     <Archives>" + used + "</Archives>" + Dump.newLine());
      bw.write("  </element>" + Dump.newLine());

      bw.write("  <element>" + Dump.newLine());
      bw.write("     <Status>Unused</Status>" + Dump.newLine());
      bw.write("     <Archives>" + unused + "</Archives>" + Dump.newLine());
      bw.write("  </element>" + Dump.newLine());

      bw.write("</elements>" + Dump.newLine());
   }

   /**
    * write out the header of the report's content
    *
    * @param bw the writer to use
    * @throws IOException if an errror occurs
    *
   public void writeHtmlBodyHeader(BufferedWriter bw) throws IOException
   {
      bw.write("<reporting>" + Dump.newLine());
      bw.write(Dump.newLine());

      bw.write("<h1>" + NAME + "</h1>" + Dump.newLine());

      bw.write("../index.xml" + Dump.newLine());
      
   }
*/
   /**
    * Create filter
    *
    * @return The filter
    */
   @Override
   protected Filter createFilter()
   {
      return new KeyFilter();
   }
}
