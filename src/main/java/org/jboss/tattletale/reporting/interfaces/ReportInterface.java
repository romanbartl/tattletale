package org.jboss.tattletale.reporting.interfaces;

import java.util.SortedSet;

public interface ReportInterface 
{
	public void generate(SortedSet<Report> dependenciesReports,
	           SortedSet<Report> generalReports,
	           SortedSet<Report> archiveReports,
	           SortedSet<Report> customReports,
	           String outputDir);
}