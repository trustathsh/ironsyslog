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

/**
 * Main class of the drools event processing eninge.
 * 
 * @author Leonard Renners
 * 
 */
public class IronSyslogDrools {

    private static String DEFAULT_DRL_FILE = "/rules/drools/eventrule.drl";

    private final KnowledgeBuilder mKbuilder = KnowledgeBuilderFactory
            .newKnowledgeBuilder();
    private Collection<KnowledgePackage> mPkgs;
    private KnowledgeBase mKbase = KnowledgeBaseFactory.newKnowledgeBase();
    private StatefulKnowledgeSession mKsession;
    private List<String> mRuleFiles;

    private static Logger LOGGER = Logger.getLogger(IronSyslogDrools.class);

    /**
     * Constructor, using the default rule file.
     */
    public IronSyslogDrools() {
        mRuleFiles = new ArrayList<>();
        mRuleFiles.add(DEFAULT_DRL_FILE);
        initialiseSession();
    }

    /**
     * Constructor, using a given rule file.
     * 
     * @param ruleFile
     *            the rule file to use
     */
    public IronSyslogDrools(String ruleFile) {
        mRuleFiles = new ArrayList<>();
        mRuleFiles.add(ruleFile);
        initialiseSession();
    }

    /**
     * Constructor, using a list of given rule files.
     * 
     * @param ruleFiles
     *            the rule files to use
     */
    public IronSyslogDrools(List<String> ruleFiles) {
        ruleFiles = new ArrayList<>();
        this.mRuleFiles.addAll(ruleFiles);
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
                System.out.println(mKbuilder.getErrors().toString());
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

}
