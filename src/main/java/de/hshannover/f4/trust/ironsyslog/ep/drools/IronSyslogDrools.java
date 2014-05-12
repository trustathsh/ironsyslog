/*
 * #%L
 * =====================================================
 *   _____                _     ____  _   _       _   _
 *  |_   _|_ __ _   _ ___| |_  / __ \| | | | ___ | | | |
 *    | | | '__| | | / __| __|/ / _` | |_| |/ __|| |_| |
 *    | | | |  | |_| \__ \ |_| | (_| |  _  |\__ \|  _  |
 *    |_| |_|   \__,_|___/\__|\ \__,_|_| |_||___/|_| |_|
 *                             \____/
 * 
 * =====================================================
 * 
 * Hochschule Hannover
 * (University of Applied Sciences and Arts, Hannover)
 * Faculty IV, Dept. of Computer Science
 * Ricklinger Stadtweg 118, 30459 Hannover, Germany
 * 
 * Email: trust@f4-i.fh-hannover.de
 * Website: http://trust.f4.hs-hannover.de/
 * 
 * This file is part of ironsyslog, version 0.0.1,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2014 Trust@HsH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package de.hshannover.f4.trust.ironsyslog.ep.drools;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.conf.EventProcessingOption;
import org.drools.definition.KnowledgePackage;
import org.drools.io.ResourceFactory;
import org.drools.runtime.ObjectFilter;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.FactHandle;
import org.drools.runtime.rule.WorkingMemoryEntryPoint;
import org.yaml.snakeyaml.Yaml;

import de.hshannover.f4.trust.ironsyslog.IronSyslog;

/**
 * Main class of the drools event processing eninge.
 * 
 * @author Leonard Renners
 * 
 */
public class IronSyslogDrools {

    private static final String RULE_FOLDER = "/rules/drools/";

    private KnowledgeBuilder mKbuilder = KnowledgeBuilderFactory
            .newKnowledgeBuilder();
    private Collection<KnowledgePackage> mPkgs;
    private KnowledgeBase mKbase = KnowledgeBaseFactory.newKnowledgeBase();
    private StatefulKnowledgeSession mKsession;
    private List<String> mRuleFiles;

    private static final Logger LOGGER = Logger
            .getLogger(IronSyslogDrools.class);

    /**
     * Constructor, using the default rule file.
     */
    public IronSyslogDrools() {
        this.mRuleFiles = new ArrayList<>();
        prepareRuleFiles();
        initialiseSession();
    }

    /**
     * Inserts a new fact into this entry point
     * 
     * @param object
     *            the fact to be inserted
     * 
     * @return the fact handle created for the given fact
     */
    public FactHandle insert(Object object) {
        return mKsession.insert(object);
    }

    /**
     * Inserts a new fact into a given entry point
     * 
     * @param object
     *            the fact to be inserted
     * @param entryPoint
     *            the entry point to use
     * 
     * @return the fact handle created for the given fact
     */
    public FactHandle insert(Object object, String entryPoint) {
        WorkingMemoryEntryPoint ep = mKsession
                .getWorkingMemoryEntryPoint(entryPoint);
        return ep.insert(object);
    }

    /**
     * Initialises the drools engine, knowledge base, rules and the session.
     */
    public void initialiseSession() {
        LOGGER.info("Initialising session...");
        if (this.mKsession == null) {
            for (String ruleFile : mRuleFiles) {
                mKbuilder.add(ResourceFactory.newClassPathResource(ruleFile,
                        IronSyslogDrools.class), ResourceType.DRL);
            }
            if (mKbuilder.hasErrors()) {
                LOGGER.error(mKbuilder.getErrors().toString());
                throw new RuntimeException("Unable to compile drl\".");
            }
            mPkgs = mKbuilder.getKnowledgePackages();

            KnowledgeBaseConfiguration config = KnowledgeBaseFactory
                    .newKnowledgeBaseConfiguration();
            config.setOption(EventProcessingOption.STREAM);
            mKbase = KnowledgeBaseFactory.newKnowledgeBase(config);

            mKbase.addKnowledgePackages(mPkgs);

            // KnowledgeSessionConfiguration conf = KnowledgeBaseFactory
            // .newKnowledgeSessionConfiguration();
            // conf.setOption(ClockTypeOption.get("pseudo"));
            // ksession = kbase.newStatefulKnowledgeSession(conf, null);
            mKsession = mKbase.newStatefulKnowledgeSession();
            // entryPoint1 = ksession.getWorkingMemoryEntryPoint("entryone");

            Thread ruleFire = new Thread() {

                @Override
                public void run() {
                    mKsession.fireUntilHalt();
                }
            };
            ruleFire.start();
        } else {
            retractAll();
        }
    }

    /**
     * Retracts the fact for which the given FactHandle was assigned.
     * 
     * @param handle
     *            the handle whose fact is to be retracted.
     */
    public void retract(FactHandle handle) {
        mKsession.retract(handle);
    }

    /**
     * Retracts several facts for which the given FactHandles were assigned.
     * 
     * @param handles
     *            the handles whose facts are to be retracted.
     */
    public void retract(List<FactHandle> handles) {
        for (FactHandle handle : handles) {
            retract(handle);
        }
    }

    /**
     * Retracts all facts.
     */
    public void retractAll() {
        LOGGER.info("Retracting all facts matching filter...");
        for (FactHandle handle : getFactHandles()) {
            retract(handle);
        }
    }

    /**
     * Retracts all facts matching the filter
     * 
     * @param filter
     *            The filter
     */
    public void retractAll(ObjectFilter filter) {
        LOGGER.info("Retracting all facts matching filter...");
        for (FactHandle handle : getFactHandles(filter)) {
            retract(handle);
        }
    }

    /**
     * Gets all fact handles matchting the filter.
     * 
     * @param filter
     *            The filter
     * @return a collection containing the fact handles matching the filter
     */
    public Collection<FactHandle> getFactHandles(ObjectFilter filter) {
        return mKsession.getFactHandles(filter);
    }

    public Collection<FactHandle> getFactHandles() {
        return mKsession.getFactHandles();
    }

    /**
     * Parses the config.yml file and prepares the list rule files which are to
     * be used
     * 
     */
    private void prepareRuleFiles() {

        if (IronSyslog.class.getResource(RULE_FOLDER) == null
                || IronSyslog.class.getResource(RULE_FOLDER + "service/") == null
                || IronSyslog.class.getResource(RULE_FOLDER + "publish/") == null
                || IronSyslog.class.getResource(RULE_FOLDER + "other/") == null) {
            LOGGER.error("Error while preparing rule files. Broken file system folder structure. \n"
                    + "Make sure that: rules/drools/ and its subfolders service/, publish/ and other/ exist");
            System.exit(1);
        }

        // Prepare Configuration
        Yaml yaml = new Yaml();
        InputStream input = IronSyslog.class.getResourceAsStream(RULE_FOLDER
                + "config.yml");
        RulesConfiguration config = yaml
                .loadAs(input, RulesConfiguration.class);

        try {
            // Add only the service rules (in the service folder) specified in
            // the configuration
            for (String service : config.getServiceInclude()) {
                String fileName = RULE_FOLDER + "service/" + service + ".drl";
                if (IronSyslog.class.getResource(fileName) != null) {
                    LOGGER.debug("Adding rule file: " + fileName);
                    mRuleFiles.add(RULE_FOLDER + "service/" + service + ".drl");
                } else {
                    LOGGER.warn("Failed to add rule file: " + fileName);
                }
            }

            // Add all publish rules (in the "publish" folder) excluding the one
            // specified on the
            // configuration
            File publishFolder = new File(IronSyslog.class.getResource(
                    RULE_FOLDER + "publish/").toURI());
            File[] publishFiles = publishFolder.listFiles();
            for (int i = 0; i < publishFiles.length; i++) {
                String fileName = publishFiles[i].getName();
                if (fileName.endsWith(".drl")
                        && !config.getPublishExclude().contains(
                                fileName.substring(0, fileName.length() - 4))) {
                    LOGGER.debug("Adding rule file: " + RULE_FOLDER
                            + "publish/" + fileName);
                    mRuleFiles.add(RULE_FOLDER + "publish/" + fileName);
                }
            }

            // Add all other rules ("other" folder, including subfolders)
            addAllRuleFilesInFolder(RULE_FOLDER + "other/");
        } catch (URISyntaxException e) {
            LOGGER.debug("Error while searching for rule files. " + e);
        }
    }

    /**
     * Adds all rule files (.drl) in the given folder and its subfolders to the
     * {@code mRuleFiles} list
     * 
     * @param path
     *            The path of the folder (starting from the resources path)
     * @throws URISyntaxException
     *             If the syntax is wrong
     */
    private void addAllRuleFilesInFolder(String path) throws URISyntaxException {
        File folder = new File(IronSyslog.class.getResource(path).toURI());
        File[] files = folder.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                addAllRuleFilesInFolder(path + f.getName());
            } else {
                String fileName = f.getName();
                if (fileName.endsWith(".drl")) {
                    LOGGER.debug("Adding rule file: " + path + fileName);
                    mRuleFiles.add(path + fileName);
                }
            }
        }
    }
}
