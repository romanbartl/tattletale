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
import org.jboss.tattletale.core.ArchiveTypes;
import org.jboss.tattletale.core.NestableArchive;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import org.jboss.tattletale.reporting.common.*;

/**
 * Transitive Depends On report
 *
 * @author Jesper Pedersen <jesper.pedersen@jboss.org>
 * @author <a href="mailto:torben.jaeger@jit-consulting.de">Torben Jaeger</a>
 */
public abstract class TransitiveDependsOnReportAbstract extends CLSReport
{
   /** NAME */
   private static final String NAME = "Transitive Depends On";

   /** DIRECTORY */
   private static final String DIRECTORY = "transitivedependson";

   /** Constructor */
   public TransitiveDependsOnReportAbstract()
   {
      super(DIRECTORY, ReportSeverity.INFO, NAME, DIRECTORY);
   }

   /**
    * write out the report's content
    *
    * @param bw the writer to use
    * @throws IOException if an error occurs
    *
   public void writeHtmlBodyContent(BufferedWriter bw) throws IOException
   {
      bw.write("<elements>" + Dump.newLine());

     
      //bw.write("     <th>Archive</th>" + Dump.newLine());
      //bw.write("     <th>Depends On</th>" + Dump.newLine());
      

      SortedMap<String, SortedSet<String>> dependsOnMap = new TreeMap<String, SortedSet<String>>();

      for (Archive archive : archives)
      {
         SortedSet<String> result = new TreeSet<String>();

         for (Archive a : archives)
         {

            if (a.getType() == ArchiveTypes.JAR)
            {
               for (String require : getRequires(a))
               {

                  if (archive.doesProvide(require) && (getCLS() == null || getCLS().isVisible(a, archive)))
                  {
                     result.add(a.getName());
                  }
               }
            }
         }

         dependsOnMap.put(archive.getName(), result);
      }

      SortedMap<String, SortedSet<String>> transitiveDependsOnMap = new TreeMap<String, SortedSet<String>>();

      Iterator mit = dependsOnMap.entrySet().iterator();
      while (mit.hasNext())
      {
         Map.Entry entry = (Map.Entry) mit.next();

         String archive = (String) entry.getKey();
         SortedSet<String> value = (SortedSet<String>) entry.getValue();

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


      mit = transitiveDependsOnMap.entrySet().iterator();
      while (mit.hasNext())
      {
         Map.Entry entry = (Map.Entry) mit.next();

         String archive = (String) entry.getKey();
         SortedSet<String> value = (SortedSet<String>) entry.getValue();

         
         
         bw.write("  <element>" + Dump.newLine());
         
        
         bw.write("     <Archive>../jar/" + archive + ".xml" + archive + "</Archive>" + Dump.newLine());
         bw.write("     <Dependson>");

         if (value.size() == 0)
         {
            bw.write("none");
         }
         else
         {
            Iterator<String> valueIt = value.iterator();
            while (valueIt.hasNext())
            {
               String r = valueIt.next();
               if (r.endsWith(".jar"))
               {
                  bw.write("../jar/" + r + ".xml" + r);
               }
               else
               {
                  if (!isFiltered(archive, r))
                  {
                     bw.write("<i>" + r + "</i>");
                     status = ReportStatus.YELLOW;
                  }
                  else
                  {
                     bw.write("<i>" + r + "</i>");
                  }
               }

               if (valueIt.hasNext())
               {
                  bw.write(", ");
               }
            }
         }

         bw.write("</Dependson>" + Dump.newLine());
         bw.write("  </element>" + Dump.newLine());

      }

      bw.write("</elements>" + Dump.newLine());
   }
*/
   private Set<String> getRequires(Archive a)
   {
      Set<String> requires = new HashSet<String>();
      if (a instanceof NestableArchive)
      {
         NestableArchive na = (NestableArchive) a;
         List<Archive> subArchives = na.getSubArchives();
         requires.addAll(na.getRequires());

         for (Archive sa : subArchives)
         {
            requires.addAll(getRequires(sa));
         }
      }
      else
      {
         requires.addAll(a.getRequires());
      }
      return requires;
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
     
   }*/

   /**
    * Get depends on
    *
    * @param scanArchive The scan archive
    * @param archive     The archive
    * @param map         The depends on map
    * @param result      The result
    */
   private void resolveDependsOn(String scanArchive, String archive,
                                 SortedMap<String, SortedSet<String>> map, SortedSet<String> result)
   {
      if (!archive.equals(scanArchive) && !result.contains(scanArchive))
      {
         result.add(scanArchive);

         SortedSet<String> value = map.get(scanArchive);
         if (value != null)
         {
            for (String aValue : value)
            {
               resolveDependsOn(aValue, archive, map, result);
            }
         }
      }
   }
}
