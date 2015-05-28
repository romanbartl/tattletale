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

import org.jboss.tattletale.Version;
import org.jboss.tattletale.core.Archive;
import org.jboss.tattletale.core.Location;
import org.jboss.tattletale.reporting.abstracts.AbstractReport;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

import org.jboss.tattletale.reporting.common.*;
import org.jboss.tattletale.reporting.interfaces.*;

/**
 * OSGi report
 *
 * @author Jesper Pedersen <jesper.pedersen@jboss.org>
 * @author <a href="mailto:torben.jaeger@jit-consulting.de">Torben Jaeger</a>
 */
public abstract class OSGiReportAbstract extends AbstractReport
{
   /** NAME */
   private static final String NAME = "OSGi";

   /** DIRECTORY */
   private static final String DIRECTORY = "osgi";

   /** Constructor */
   public OSGiReportAbstract()
   {
      super(DIRECTORY, ReportSeverity.INFO, NAME, DIRECTORY);
   }

   /**
    * Generate the report(s)
    *
    * @param outputDirectory The top-level output directory
    */
   @Override
   public void generate(String outputDirectory)
   {
      super.generate(outputDirectory);

      try
      {
         for (Archive archive : archives)
         {
            List<String> osgiInformation = getOSGIInfo(archive);

            File archiveOutput = writeArchiveOSGIHtml(archive, osgiInformation);

            writeArchiveOSGIManifest(archive, osgiInformation, archiveOutput);
         }

      }
      catch (Exception e)
      {
         System.err.println("OSGiReport: " + e.getMessage());
         e.printStackTrace(System.err);
      }
   }

   private void writeArchiveOSGIManifest(Archive archive, List<String> osgiInformation, File archiveOutput)
      throws IOException
   {
      FileWriter mfw = new FileWriter(archiveOutput.getAbsolutePath() + File.separator + "MANIFEST.MF");
      BufferedWriter mbw = new BufferedWriter(mfw, 8192);

      if (archive.getManifest() != null)
      {
         for (String s : archive.getManifest())
         {
            mbw.write(s);
            mbw.write(Dump.newLine());
         }
      }

      if (!archive.isOSGi() && osgiInformation != null && osgiInformation.size() > 0)
      {
         mbw.write(Dump.newLine());
         mbw.write("### OSGi information" + Dump.newLine());
         for (String anOsgiInformation : osgiInformation)
         {
            if (anOsgiInformation.length() <= 69)
            {
               mbw.write(anOsgiInformation);
               mbw.write(Dump.newLine());
            }
            else
            {
               int count = 0;
               for (int i = 0; i < anOsgiInformation.length(); i++)
               {
                  char c = anOsgiInformation.charAt(i);
                  if (count <= 69)
                  {
                     mbw.write(c);
                     count++;
                  }
                  else
                  {
                     mbw.write(Dump.newLine());
                     mbw.write(' ');
                     mbw.write(c);
                     count = 2;
                  }
               }
               mbw.write(Dump.newLine());
            }
         }
      }

      mbw.flush();
      mbw.close();
   }
/*
   private File writeArchiveOSGIHtml(Archive archive, List<String> osgiInformation) throws IOException
   {
      File archiveOutput = new File(getOutputDirectory(), archive.getName());
      archiveOutput.mkdirs();

      FileWriter rfw = new FileWriter(archiveOutput.getAbsolutePath() + File.separator + "index.xml");
      BufferedWriter rbw = new BufferedWriter(rfw, 8192);
      rbw.write("<?xml version=\"1.0\" encoding=\"utf-8\"?> "+ Dump.newLine());
      rbw.write("<main>" + Dump.newLine());
      rbw.write("<info>" + Dump.newLine());
      rbw.write("  <title>" + Version.FULL_VERSION + ": " + NAME + " - " + archive.getName()
               + "</title>" + Dump.newLine());
    
      rbw.write("</info>" + Dump.newLine());
      rbw.write("<reporting>" + Dump.newLine());
      rbw.write(Dump.newLine());

      rbw.write("<h1>" + NAME + " - " + archive.getName() + "</h1>" + Dump.newLine());

      rbw.write("../index.xml" + Dump.newLine());
     

      rbw.write("<elements>" + Dump.newLine());

    
      //rbw.write("     <th>Field</th>" + Dump.newLine());
      //rbw.write("     <th>Value</th>" + Dump.newLine());
     

      rbw.write("  <element>" + Dump.newLine());
      rbw.write("     <t>OSGi</t>" + Dump.newLine());
      if (archive.isOSGi())
      {
         rbw.write("     <Status>Yes</Status>" + Dump.newLine());
      }
      else
      {
         rbw.write("     <Status>No</Status>" + Dump.newLine());
      }
      rbw.write("  </element>" + Dump.newLine());

      rbw.write("  <element>" + Dump.newLine());
      rbw.write("     <Field>Manifest</Field>" + Dump.newLine());
      rbw.write("     <Value>");

      if (archive.getManifest() != null)
      {
         for (String s : archive.getManifest())
         {
            rbw.write(s);
         }
      }

      rbw.write("</Value>" + Dump.newLine());
      rbw.write("  </element>" + Dump.newLine());

      if (!archive.isOSGi())
      {
         rbw.write("  <element>" + Dump.newLine());
         rbw.write("     <Field>OSGi Manifest</Field>" + Dump.newLine());
         rbw.write("     <Value>");

         if (osgiInformation != null && osgiInformation.size() > 0)
         {
            for (String anOsgiInformation : osgiInformation)
            {
               rbw.write(anOsgiInformation);
              
            }
         }

         rbw.write("</Value>" + Dump.newLine());
         rbw.write("  </element>" + Dump.newLine());
      }

      rbw.write("</elements>" + Dump.newLine());

      rbw.write(Dump.newLine());
     
      rbw.write("Generated by:  http://www.jboss.org/tattletale");
      rbw.write(Dump.newLine());
      rbw.write("</reporting>" + Dump.newLine());
      rbw.write("</main>" + Dump.newLine());

      rbw.flush();
      rbw.close();
      return archiveOutput;
   }
   */

   private List<String> getOSGIInfo(Archive archive)
   {
      List<String> osgiInformation = null;

      if (!archive.isOSGi())
      {
         osgiInformation = new ArrayList<String>();

         SortedMap<String, Integer> exportPackages = new TreeMap<String, Integer>();

         for (String provide : archive.getProvides().keySet())
         {
            if (provide.lastIndexOf(".") != -1)
            {
               String packageName = provide.substring(0, provide.lastIndexOf("."));
               Integer number = exportPackages.get(packageName);
               if (number == null)
               {
                  number = 0;
               }

               number = number + 1;
               exportPackages.put(packageName, number);
            }
         }

         SortedMap<String, String> importPackages = new TreeMap<String, String>();

         for (String require : archive.getRequires())
         {

            if (require.lastIndexOf(".") != -1)
            {
               String packageName = require.substring(0, require.lastIndexOf("."));

               if (importPackages.get(packageName) == null)
               {
                  String version = null;
                  boolean found = false;

                  Iterator<Archive> ait = archives.iterator();
                  while (!found && ait.hasNext())
                  {
                     Archive a = ait.next();

                     if (a.doesProvide(require))
                     {
                        version = a.getLocations().first().getVersion();

                        if (version == null)
                        {
                           version = "0.0.0";
                        }

                        found = true;
                     }
                  }

                  importPackages.put(packageName, version);
               }
            }
         }

         osgiInformation.add("Bundle-ManifestVersion: 2");

         String bundleSymbolicName;
         if (exportPackages.size() > 0)
         {
            String bsn = null;
            Integer bsnv = null;

            for (Map.Entry<String, Integer> entry : exportPackages.entrySet())
            {
               String pkg = entry.getKey();
               Integer v = entry.getValue();

               if (bsn == null)
               {
                  bsn = pkg;
                  bsnv = v;
               }
               else
               {
                  if (v > bsnv)
                  {
                     bsn = pkg;
                     bsnv = v;
                  }
               }
            }

            bundleSymbolicName = bsn;
         }
         else
         {
            bundleSymbolicName = "UNKNOWN";
         }
         osgiInformation.add("Bundle-SymbolicName: " + bundleSymbolicName);

         String bundleDescription = archive.getName().substring(0, archive.getName().lastIndexOf("."));
         osgiInformation.add("Bundle-Description: " + bundleDescription);

         String bName = archive.getName().substring(0, archive.getName().lastIndexOf("."));
         StringBuffer bundleName = new StringBuffer();
         for (int i = 0; i < bName.length(); i++)
         {
            char c = bName.charAt(i);
            if (c != '\n' && c != '\r' && c != ' ')
            {
               bundleName = bundleName.append(c);
            }
         }
         osgiInformation.add("Bundle-Name: " + bundleName.toString());

         Location location = archive.getLocations().first();
         String bundleVersion = getOSGiVersion(location.getVersion());
         osgiInformation.add("Bundle-Version: " + bundleVersion);

         StringBuffer exportPackage = new StringBuffer();
         Iterator<String> eit = exportPackages.keySet().iterator();
         while (eit.hasNext())
         {
            String ep = eit.next();

            exportPackage = exportPackage.append(ep);

            SortedSet<String> epd = archive.getPackageDependencies().get(ep);
            if (epd != null && epd.size() > 0)
            {
               exportPackage = exportPackage.append(";uses:=\"");

               Iterator<String> epdi = epd.iterator();
               while (epdi.hasNext())
               {
                  exportPackage = exportPackage.append(epdi.next());

                  if (epdi.hasNext())
                  {
                     exportPackage = exportPackage.append(",");
                  }
               }

               exportPackage = exportPackage.append("\"");
            }

            if (eit.hasNext())
            {
               exportPackage = exportPackage.append(",");
            }
         }
         osgiInformation.add("Export-Package: " + exportPackage.toString());

         StringBuffer importPackage = new StringBuffer();
         Iterator iit = importPackages.entrySet().iterator();
         while (iit.hasNext())
         {
            Map.Entry entry = (Map.Entry) iit.next();

            String pkg = (String) entry.getKey();
            String v = (String) entry.getValue();

            importPackage = importPackage.append(pkg);

            if (v != null)
            {
               importPackage = importPackage.append(";version=\"");
               importPackage = importPackage.append(getOSGiVersion(v));
               importPackage = importPackage.append("\"");
            }

            if (iit.hasNext())
            {
               importPackage = importPackage.append(",");
            }
         }
         osgiInformation.add("Import-Package: " + importPackage.toString());
      }
      return osgiInformation;
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

     
     /* bw.write("     <th>Archive</th>" + Dump.newLine());
      bw.write("     <th>OSGi</th>" + Dump.newLine());
      bw.write("     <th>Report</th>" + Dump.newLine());
      bw.write("     <th>Manifest</th>" + Dump.newLine());*
 

      boolean odd = true;

      int osgiReady = 0;
      int osgiNotReady = 0;

      for (Archive archive : archives)
      {
         String archiveName = archive.getName();
         int finalDot = archiveName.lastIndexOf(".");
         String extension = archiveName.substring(finalDot + 1);

         
         bw.write("  <element>" + Dump.newLine());
        
        
         bw.write("     <Archive>../" + extension + "/" + archiveName + ".xml" +
                  archiveName + "</Archive>" + Dump.newLine());
         if (archive.isOSGi())
         {
            bw.write("     <OSGi>Yes</OSGi>" + Dump.newLine());
            osgiReady++;
         }
         else
         {
            osgiNotReady++;

            if (!isFiltered(archiveName))
            {
               status = ReportStatus.RED;
               bw.write("     <OSGi>No</OSGi>" + Dump.newLine());
            }
            else
            {
               bw.write("     <OSGi>No</OSGi>" + Dump.newLine());
            }
         }
         bw.write("     <Report>" + archiveName + "/index.xml</Report>" + Dump.newLine());
         bw.write("     <Manifest>" + archiveName + "/MANIFEST.MF</Manifest>" + Dump.newLine());
         bw.write("  </element>" + Dump.newLine());

         odd = !odd;
      }

      bw.write("</elements>" + Dump.newLine());

      bw.write(Dump.newLine());
      

      bw.write("<elements>" + Dump.newLine());

      bw.write("  <element>" + Dump.newLine());
      //bw.write("     <th>Status</th>" + Dump.newLine());
      //bw.write("     <th>Archives</th>" + Dump.newLine());
      bw.write("  </element>" + Dump.newLine());

      bw.write("  <element class=\"rowodd\">" + Dump.newLine());
      bw.write("     <Status>Ready</Status>" + Dump.newLine());
      bw.write("     <Archives>" + osgiReady + "</Archives>" + Dump.newLine());
      bw.write("  </element>" + Dump.newLine());

      bw.write("  <element class=\"roweven\">" + Dump.newLine());
      bw.write("     <Status>Not ready</Status>" + Dump.newLine());
      bw.write("     <Archives>" + osgiNotReady + "</Archives>" + Dump.newLine());
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
    * Get Bundle-Version
    *
    * @param version The archive version
    * @return OSGi version
    */
   private String getOSGiVersion(String version)
   {
      if (version == null)
      {
         return "0.0.0";
      }

      if (!version.matches("\\d+(\\.\\d+(\\.\\d+(\\.[0-9a-zA-Z\\_\\-]+)?)?)?"))
      {
         return "0.0.0";
      }

      return version;
   }

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
