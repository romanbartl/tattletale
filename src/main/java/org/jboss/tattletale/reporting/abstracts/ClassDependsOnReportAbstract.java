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

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.jboss.tattletale.reporting.common.*;
/**
 * Class level Depends On report
 *
 * @author Jesper Pedersen <jesper.pedersen@jboss.org>
 */
public abstract class ClassDependsOnReportAbstract extends CLSReportAbstract
{
   /** NAME */
   public static final String NAME = "Class Depends On";

   /** DIRECTORY */
   public static final String DIRECTORY = "classdependson";


   /** Constructor */
   public ClassDependsOnReportAbstract()
   {
      super(DIRECTORY, ReportSeverity.INFO, NAME, DIRECTORY);
   }
   protected SortedMap<String, SortedSet<String>> getClassDependencies(Archive archive)
   {
      SortedMap<String, SortedSet<String>> classDeps = new TreeMap<String, SortedSet<String>>();

      if (archive instanceof NestableArchive)
      {
         NestableArchive nestableArchive = (NestableArchive) archive;
         List<Archive> subArchives = nestableArchive.getSubArchives();

         for (Archive sa : subArchives)
         {
            classDeps.putAll(getClassDependencies(sa));
         }

         classDeps.putAll(nestableArchive.getClassDependencies());
      }
      else
      {
         classDeps.putAll(archive.getClassDependencies());
      }
      return classDeps;
   }
}
