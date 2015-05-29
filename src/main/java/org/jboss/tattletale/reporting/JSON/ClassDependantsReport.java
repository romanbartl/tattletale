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
import java.util.TreeMap;
import java.util.TreeSet;

import org.jboss.tattletale.core.Archive;
import org.jboss.tattletale.profiles.Profile;
import org.jboss.tattletale.reporting.abstracts.ClassDependantsReportAbstract;
/**
 * Class level Dependants report
 *
 * @author Jesper Pedersen <jesper.pedersen@jboss.org>
 */
public class ClassDependantsReport extends ClassDependantsReportAbstract
{
	public void writeJSON(BufferedWriter bw) throws IOException
	{
		bw.write("{"); 
		bw.write("\"main\":\"../index.json\",");
		bw.write("\"classes\":[");
		
		SortedMap<String, SortedSet<String>> result = new TreeMap<String, SortedSet<String>>();


	    for (Archive archive : archives)
	    {
	       SortedMap<String, SortedSet<String>> classDependencies = getClassDependencies(archive);

	       Iterator<Map.Entry<String, SortedSet<String>>> dit = classDependencies.entrySet().iterator();
	       
	       while (dit.hasNext())
	       {
	          Map.Entry<String, SortedSet<String>> entry = dit.next();
	          String clz = entry.getKey();
	          SortedSet<String> clzDeps = entry.getValue();

	          Iterator<String> sit = clzDeps.iterator();
	          while (sit.hasNext())
	          {
	             String dep = sit.next();
  
	             if (!dep.equals(clz))
	             {
	                boolean include = true;

	                Iterator<Profile> kit = getKnown().iterator();
	                while (include && kit.hasNext())
	                {
	                   Profile profile = kit.next();

	                   if (profile.doesProvide(dep))
	                   {
	                      include = false;
	                   }
	                }

	                if (include)
	                {
	                   SortedSet<String> deps = result.get(dep);

	                   if (deps == null)
	                   {
	                      deps = new TreeSet<String>();
	                   }

	                   deps.add(clz);

	                   result.put(dep, deps);
	                }
	             }
	            }
	         }
	      }

	      Iterator<Map.Entry<String, SortedSet<String>>> rit = result.entrySet().iterator();

	      while (rit.hasNext())
	      {
	    	 bw.write("{");
	    	  
	         Map.Entry<String, SortedSet<String>> entry = rit.next();
	         String clz = entry.getKey();
	         SortedSet<String> deps = entry.getValue();

	         if (deps != null && deps.size() > 0)
	         {
	        	 bw.write("\"class\":\"" + clz + "\",");
	        	 bw.write("\"dependants\":[");

	             Iterator<String> sit = deps.iterator();
	             while (sit.hasNext())
	             {
 	                String dep = sit.next();
 	                bw.write("\"" + dep + "\"");

	                if (sit.hasNext()) bw.write(", ");
	             }
	             
	             bw.write("]");
	         }
	         
	         bw.write("}");
	         
	         if (rit.hasNext()) bw.write(",");  
	      }	
	      
	      bw.write("]}");

	}
}
