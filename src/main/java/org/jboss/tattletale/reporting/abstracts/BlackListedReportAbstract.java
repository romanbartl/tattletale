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

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

import org.jboss.tattletale.core.Archive;
import org.jboss.tattletale.core.NestableArchive;
import org.jboss.tattletale.reporting.abstracts.AbstractReport;
import org.jboss.tattletale.reporting.common.ReportSeverity;
import org.jboss.tattletale.reporting.common.ReportStatus;
import org.jboss.tattletale.reporting.interfaces.Filter;
/**
 * Blacklisted report
 *
 * @author Jesper Pedersen <jesper.pedersen@jboss.org>
 * @author <a href="mailto:torben.jaeger@jit-consulting.de">Torben Jaeger</a>
 */
public abstract class BlackListedReportAbstract extends AbstractReport
{
   /** NAME */
   protected static final String NAME = "Black listed";

   /** DIRECTORY */
   protected static final String DIRECTORY = "blacklisted";

   /** Constructor */
   public BlackListedReportAbstract()
   {
      super(DIRECTORY, ReportSeverity.ERROR, NAME, DIRECTORY);
   }

   protected SortedMap<String, SortedSet<String>> getBlackListedDeps(Archive a)
   {
      SortedMap<String, SortedSet<String>> deps = new TreeMap<String, SortedSet<String>>();
      if (a instanceof NestableArchive)
      {
         NestableArchive na = (NestableArchive) a;
         List<Archive> subArchives = na.getSubArchives();

         for (Archive sa : subArchives)
         {
            deps.putAll(getBlackListedDeps(sa));
         }
      }
      else
      {
         deps.putAll(a.getBlackListedDependencies());
      }
      return deps;
   }

   @Override
   protected Filter createFilter()
   {
      return new KeyFilterAbstract();
   }
}
