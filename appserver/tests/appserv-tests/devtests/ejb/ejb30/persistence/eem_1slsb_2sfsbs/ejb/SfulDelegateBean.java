/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package com.sun.s1asdev.ejb.ejb30.persistence.eem_1slsb_2sfsbs;

import javax.ejb.Stateful;
import javax.ejb.EJB;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceContext;
import javax.persistence.EntityNotFoundException;

import javax.persistence.EntityManager;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateful
public class SfulDelegateBean
    implements SfulDelegate {

    @PersistenceContext(
        unitName="lib/ejb-ejb30-persistence-eem_1slsb_2sfsbs-par1.jar#em",
        type=PersistenceContextType.EXTENDED)
    private EntityManager extendedEM;

    @EJB
    private SfulPeer sfulPeer;

    public EntityManager getEM() {
        return extendedEM;
    }
    
    public Person create(String name, String data) {

        Person p = new Person(name, data);
        
        extendedEM.persist(p);
        return p;
    }

    public Person find(String name) {

        Person p = extendedEM.find(Person.class, name);
        System.out.println("Found " + p);
        return p;
    }

    public boolean remove(String name) {

        Person p = extendedEM.find(Person.class, name);
        boolean removed = false;
        if (p != null) {
            extendedEM.remove(p);
            removed = true;
        }
        return removed;
    }

    public SfulPeer getSfulPeer() {
        return sfulPeer;
    }

}
