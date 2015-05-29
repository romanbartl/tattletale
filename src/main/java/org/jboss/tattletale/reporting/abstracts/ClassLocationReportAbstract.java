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

import java.util.SortedMap;
import java.util.SortedSet;

import org.jboss.tattletale.reporting.common.ReportSeverity;
import org.jboss.tattletale.reporting.interfaces.Filter;
/**
 * Class location report
 *
 * @author Jesper Pedersen <jesper.pedersen@jboss.org>
 * @author <a href="mailto:torben.jaeger@jit-consulting.de">Torben Jaeger</a>
 */
public abstract class ClassLocationReportAbstract extends AbstractReport
{
   /** NAME */
   public static final String NAME = "Class Location";

   /** DIRECTORY */
   public static final String DIRECTORY = "classlocation";

   /** Globally provides */
   protected SortedMap<String, SortedSet<String>> gProvides;

   /** Constructor */
   public ClassLocationReportAbstract()
   {
      super(DIRECTORY, ReportSeverity.INFO, NAME, DIRECTORY);
   }

   /**
    * Set the globally provides map to be used in generating this report
    *
    * @param gProvides the map of global provides
    */
   public void setGlobalProvides(SortedMap<String, SortedSet<String>> gProvides)
   {
      this.gProvides = gProvides;
   }

    @Override
   protected Filter createFilter()
   {
      return new KeyFilterAbstract();
   }
}
