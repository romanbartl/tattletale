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
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.jboss.tattletale.core.Archive;
import org.jboss.tattletale.profiles.ExtendedProfile;
import org.jboss.tattletale.profiles.JBossAS7Profile;
import org.jboss.tattletale.profiles.Profile;
import org.jboss.tattletale.reporting.abstracts.AS7ReportAbstract;

/**
 * Report type that makes use of the {@link org.jboss.tattletale.profiles.ExtendedProfile} to find which module
 * identifiers it needs for the scanned archives (eg: .war, .ear)
 *
 * @author Navin Surtani
 */

public class AS7Report extends AS7ReportAbstract
{

   public void writeJSON(BufferedWriter bw) throws IOException
   {
	   bw.write("{\"main\":\"../index.json\"");
	   bw.write("\"archives\":[");
	   
	   for (Archive archive : archives)
	   {
	      Set<String> provides = getProvides(archive);
	      Set<String> requires = getRequires(archive);
	      
	      requires.removeAll(provides);
	      String archiveName = archive.getName();
	      int finalDot = archiveName.lastIndexOf(".");
	      String extension = archiveName.substring(finalDot + 1);
	      
	      File deploymentJSON = buildDeploymentJson(requires, archiveName);
	      String path = "./" + archiveName + "/" + deploymentJSON.getName();

	      bw.write("{");
	      	
	      bw.write("\"archive\":");
	      bw.write("\"" + archiveName + "\",");
	      bw.write("\"archive_link\":");
	      bw.write("\"../" + extension + "/" + archiveName + ".json\",");
	      bw.write("\"jboss_deployment\":");
	      bw.write("\"jboss-deployment-structure\",");
	      bw.write("\"jboss_deployment_link\":");
	      bw.write("\"" + path + "\"");

	      bw.write("}");
	   }
	   
	   bw.write("]}");
	   
   }
   
   private File buildDeploymentJson(Set<String> requires, String archiveName) throws IOException
   {
	   File deployedDir = new File(getOutputDirectory(), archiveName);
	   deployedDir.mkdirs();
	   File outputJSON = new File(deployedDir.getAbsolutePath() + File.separator + "jboss-deployment-structure.xml");
	   FileWriter fw = new FileWriter(outputJSON);
	   BufferedWriter bw = new BufferedWriter(fw, 8192);
	   
	   
	   /*
	   bw.write("<?xml version=\"1.0\"?>" + Dump.newLine());
	      bw.write("<jboss-deployment-structure>" + Dump.newLine());
	      bw.write("  <deployment>" + Dump.newLine());
	      bw.write("     <dependencies>" + Dump.newLine());

	      ExtendedProfile as7Profile = new JBossAS7Profile();
	      SortedSet<String> moduleIdentifiers = new TreeSet<String>();

	      for (String requiredClass : requires)
	      {
	         String moduleIdentifier = as7Profile.getModuleIdentifier(requiredClass);
	         if (moduleIdentifier != null)
	         {
	            moduleIdentifiers.add(moduleIdentifier);
	         }
	         else
	         {
	            for (Profile p : getKnown())
	            {
	               if (p.doesProvide(requiredClass))
	               {
	                  moduleIdentifier = p.getModuleIdentifier();
	                  if (moduleIdentifier != null)
	                  {
	                     moduleIdentifiers.add(moduleIdentifier);
	                  }
	               }
	            }
	         }
	      }

	      for (String identifier : moduleIdentifiers)
	      {
	         bw.write("        <module name=\"" + identifier + "\"/>" + Dump.newLine());
	      }
	      bw.write("     </dependencies>" + Dump.newLine());
	      bw.write("  </deployment>" + Dump.newLine());
	      bw.write("</jboss-deployment-structure>" + Dump.newLine());
	      bw.flush();
	      bw.close();	
	   */   
	   
	   return outputJSON;
   }

}
