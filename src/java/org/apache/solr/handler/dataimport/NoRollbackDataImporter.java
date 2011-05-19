package org.apache.solr.handler.dataimport;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.dataimport.DataImporter.RequestParams;
import org.apache.solr.handler.dataimport.DataImporter.Status;
import org.apache.solr.schema.IndexSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoRollbackDataImporter extends DataImporter {
	
	private static final Logger LOG = LoggerFactory.getLogger(NoRollbackDataImporter.class);
	private ReentrantLock importLock = new ReentrantLock();
	
	
	NoRollbackDataImporter() {
		super();
	  }

	NoRollbackDataImporter(String dataConfig, SolrCore core, Map<String, Properties> ds, Map<String, Object> session) {
		super(dataConfig, core, ds, session);
	}

	
	public boolean isBusy() {
	    return importLock.isLocked();
	  }
	
	public void doFullImport(SolrWriter writer, RequestParams requestParams) {
	    LOG.info("Starting Full Import");
	    setStatus(Status.RUNNING_FULL_DUMP);

	    setIndexStartTime(new Date());

	    try {
	      docBuilder = new DocBuilder(this, writer, requestParams);
	      docBuilder.execute();
	      if (!requestParams.debug)
	        cumulativeStatistics.add(docBuilder.importStatistics);
	    } catch (Throwable t) {
	      LOG.error("Full Import failed", t);
	      //docBuilder.rollback();
	    } finally {
	      setStatus(Status.IDLE);
	      super.getConfig().clearCaches();
	      DocBuilder.INSTANCE.set(null);
	    }

	  }

	  public void doDeltaImport(SolrWriter writer, RequestParams requestParams) {
	    LOG.info("Starting Delta Import");
	    setStatus(Status.RUNNING_DELTA_DUMP);

	    try {
	      setIndexStartTime(new Date());
	      docBuilder = new DocBuilder(this, writer, requestParams);
	      docBuilder.execute();
	      if (!requestParams.debug)
	        cumulativeStatistics.add(docBuilder.importStatistics);
	    } catch (Throwable t) {
	      LOG.error("Delta Import Failed", t);
	      //docBuilder.rollback();
	    } finally {
	      setStatus(Status.IDLE);
	      super.getConfig().clearCaches();
	      DocBuilder.INSTANCE.set(null);
	    }

	  }

	void runCmd(RequestParams reqParams, SolrWriter sw) {
	    String command = reqParams.command;
	    if (command.equals(ABORT_CMD)) {
	      if (docBuilder != null) {
	        docBuilder.abort();
	      }
	      return;
	    }
	    if (!importLock.tryLock()){
	      LOG.warn("Import command failed . another import is running");      
	      return;
	    }
	    try {
	      if (FULL_IMPORT_CMD.equals(command) || IMPORT_CMD.equals(command)) {
	        doFullImport(sw, reqParams);
	      } else if (command.equals(DELTA_IMPORT_CMD)) {
	        doDeltaImport(sw, reqParams);
	      }
	    } finally {
	      importLock.unlock();
	    }
	  }


}
