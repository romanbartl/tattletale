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
import org.jboss.tattletale.core.NestableArchive;
import org.jboss.tattletale.profiles.ExtendedProfile;
import org.jboss.tattletale.profiles.JBossAS7Profile;
import org.jboss.tattletale.profiles.Profile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.jboss.tattletale.reporting.common.*;
/**
 * Report type that makes use of the {@link org.jboss.tattletale.profiles.ExtendedProfile} to find which module
 * identifiers it needs for the scanned archives (eg: .war, .ear)
 *
 * @author Navin Surtani
 */
public abstract class AS7ReportAbstract extends CLSReportAbstract
{
   /** NAME **/
   private static final String NAME = "JBoss AS7";

   /** DIRECTORY */
   private static final String DIRECTORY = "jboss-as7";

   /** Constructor */
   public AS7ReportAbstract()
   {
      super(NAME, ReportSeverity.INFO, NAME, DIRECTORY);
   }
   
   protected Set<String> getProvides(Archive a)
   {
      Set<String> provides = new HashSet<String>();
      if (a instanceof NestableArchive)
      {
         NestableArchive na = (NestableArchive) a;
         List<Archive> subArchives = na.getSubArchives();
         provides.addAll(na.getProvides().keySet());

         for (Archive sa : subArchives)
         {
            provides.addAll(getProvides(sa));
         }
      }
      else
      {
         provides.addAll(a.getProvides().keySet());
      }
      return provides;
   }

   protected Set<String> getRequires(Archive a)
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

}
