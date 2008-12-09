/* $RCSfile$
 * $Author$    
 * $Date$    
 * $Revision$
 * 
 * Copyright (C) 1997-2007  The Chemistry Development Kit (CDK) project
 * 
 * Contact: cdk-devel@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA. 
 * 
 */
package org.openscience.cdk;

import java.util.Hashtable;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IBioPolymer;
import org.openscience.cdk.interfaces.IMonomer;
import org.openscience.cdk.interfaces.IStrand;

/**
 * Checks the functionality of the BioPolymer class.
 *
 * @cdk.module test-data
 *
 * @see org.openscience.cdk.BioPolymer
 */
public class BioPolymerTest extends PolymerTest {

    @BeforeClass public static void setUp() {
        setBuilder(DefaultChemObjectBuilder.getInstance());
    }

	@Test public void testBioPolymer() {
		IBioPolymer oBioPolymer = getBuilder().newBioPolymer();
		Assert.assertNotNull(oBioPolymer);
		Assert.assertEquals(oBioPolymer.getMonomerCount(), 0);
		
		IStrand oStrand1 = getBuilder().newStrand();
		oStrand1.setStrandName("A");
		IStrand oStrand2 = getBuilder().newStrand();
		oStrand2.setStrandName("B");
		IMonomer oMono1 = getBuilder().newMonomer();
		oMono1.setMonomerName(new String("TRP279"));
		IMonomer oMono2 = getBuilder().newMonomer();
		oMono2.setMonomerName(new String("HOH"));
		IMonomer oMono3 = getBuilder().newMonomer();
		oMono3.setMonomerName(new String("GLYA16"));
		IAtom oAtom1 = getBuilder().newAtom("C1");
		IAtom oAtom2 = getBuilder().newAtom("C2");
		IAtom oAtom3 = getBuilder().newAtom("C3");
		IAtom oAtom4 = getBuilder().newAtom("C4");
		IAtom oAtom5 = getBuilder().newAtom("C5");
		
		oBioPolymer.addAtom(oAtom1);
		oBioPolymer.addAtom(oAtom2, oStrand1);
		oBioPolymer.addAtom(oAtom3, oMono1, oStrand1);
		oBioPolymer.addAtom(oAtom4, oMono2, oStrand2);
		oBioPolymer.addAtom(oAtom5, oMono3, oStrand2);
		Assert.assertNotNull(oBioPolymer.getAtom(0));
		Assert.assertNotNull(oBioPolymer.getAtom(1));
		Assert.assertNotNull(oBioPolymer.getAtom(2));
		Assert.assertNotNull(oBioPolymer.getAtom(3));
		Assert.assertNotNull(oBioPolymer.getAtom(4));
		Assert.assertEquals(oAtom1, oBioPolymer.getAtom(0));
		Assert.assertEquals(oAtom2, oBioPolymer.getAtom(1));
		Assert.assertEquals(oAtom3, oBioPolymer.getAtom(2));
		Assert.assertEquals(oAtom4, oBioPolymer.getAtom(3));
		Assert.assertEquals(oAtom5, oBioPolymer.getAtom(4));

		Assert.assertNull(oBioPolymer.getMonomer("0815", "A"));
		Assert.assertNull(oBioPolymer.getMonomer("0815", "B"));
		Assert.assertNull(oBioPolymer.getMonomer("0815", ""));
		Assert.assertNull(oBioPolymer.getStrand(""));
		Assert.assertNotNull(oBioPolymer.getMonomer("TRP279", "A"));
		Assert.assertEquals(oMono1, oBioPolymer.getMonomer("TRP279", "A"));
		Assert.assertEquals(oBioPolymer.getMonomer("TRP279", "A").getAtomCount(), 1);
		Assert.assertNotNull(oBioPolymer.getMonomer("HOH", "B"));
		Assert.assertEquals(oMono2, oBioPolymer.getMonomer("HOH", "B"));
		Assert.assertEquals(oBioPolymer.getMonomer("HOH", "B").getAtomCount(), 1);
		Assert.assertEquals(oBioPolymer.getStrand("B").getAtomCount(), 2);
		Assert.assertEquals(oBioPolymer.getStrand("B").getMonomerCount(), 2);
		Assert.assertNull(oBioPolymer.getStrand("C"));
		Assert.assertNotNull(oBioPolymer.getStrand("B"));
	}
	
	@Test public void testGetMonomerCount() {
	    IBioPolymer oBioPolymer = getBuilder().newBioPolymer();
		Assert.assertEquals(0, oBioPolymer.getMonomerCount());
		
		IStrand oStrand1 = getBuilder().newStrand();
		oStrand1.setStrandName("A");
		IStrand oStrand2 = getBuilder().newStrand();
		oStrand2.setStrandName("B");
		IMonomer oMono1 = getBuilder().newMonomer();
		oMono1.setMonomerName(new String("TRP279"));
		IMonomer oMono2 = getBuilder().newMonomer();
		oMono2.setMonomerName(new String("HOH"));
		IAtom oAtom1 = getBuilder().newAtom("C1");
		IAtom oAtom2 = getBuilder().newAtom("C2");
		IAtom oAtom3 = getBuilder().newAtom("C3");
		oBioPolymer.addAtom(oAtom1);
		oBioPolymer.addAtom(oAtom2, oMono1, oStrand1);
		oBioPolymer.addAtom(oAtom3, oMono2, oStrand2);
		Assert.assertNotNull(oBioPolymer.getAtom(0));
		Assert.assertNotNull(oBioPolymer.getAtom(1));
		Assert.assertNotNull(oBioPolymer.getAtom(2));
		Assert.assertEquals(oAtom1, oBioPolymer.getAtom(0));
		Assert.assertEquals(oAtom2, oBioPolymer.getAtom(1));
		Assert.assertEquals(oAtom3, oBioPolymer.getAtom(2));

		Assert.assertEquals(2, oBioPolymer.getMonomerCount());
	}
	
	@Test public void testGetMonomerNames() {
	    IBioPolymer oBioPolymer = getBuilder().newBioPolymer();
		Assert.assertEquals(0, oBioPolymer.getMonomerNames().size());
		
		IStrand oStrand1 = getBuilder().newStrand();
		oStrand1.setStrandName("A");
		IStrand oStrand2 = getBuilder().newStrand();
		oStrand2.setStrandName("B");
		IMonomer oMono1 = getBuilder().newMonomer();
		oMono1.setMonomerName(new String("TRP279"));
		IMonomer oMono2 = getBuilder().newMonomer();
		oMono2.setMonomerName(new String("HOH"));
		IAtom oAtom1 = getBuilder().newAtom("C1");
		IAtom oAtom2 = getBuilder().newAtom("C2");
		IAtom oAtom3 = getBuilder().newAtom("C3");
		oBioPolymer.addAtom(oAtom1);
		oBioPolymer.addAtom(oAtom2, oMono1, oStrand1);
		oBioPolymer.addAtom(oAtom3, oMono2, oStrand2);
		Assert.assertNotNull(oBioPolymer.getAtom(0));
		Assert.assertNotNull(oBioPolymer.getAtom(1));
		Assert.assertNotNull(oBioPolymer.getAtom(2));
		Assert.assertEquals(oAtom1, oBioPolymer.getAtom(0));
		Assert.assertEquals(oAtom2, oBioPolymer.getAtom(1));
		Assert.assertEquals(oAtom3, oBioPolymer.getAtom(2));

		Assert.assertEquals(3, oBioPolymer.getMonomerNames().size());
		Assert.assertTrue(oBioPolymer.getMonomerNames().contains(oMono1.getMonomerName()));
		Assert.assertTrue(oBioPolymer.getMonomerNames().contains(oMono2.getMonomerName()));
	}
	
	@Test public void testGetMonomer_String_String() {
	    IBioPolymer oBioPolymer = getBuilder().newBioPolymer();
		
	    IStrand oStrand1 = getBuilder().newStrand();
		oStrand1.setStrandName("A");
		IStrand oStrand2 = getBuilder().newStrand();
		oStrand2.setStrandName("B");
		IMonomer oMono1 = getBuilder().newMonomer();
		oMono1.setMonomerName(new String("TRP279"));
		IMonomer oMono2 = getBuilder().newMonomer();
		oMono2.setMonomerName(new String("HOH"));
		IAtom oAtom1 = getBuilder().newAtom("C1");
		IAtom oAtom2 = getBuilder().newAtom("C2");
		IAtom oAtom3 = getBuilder().newAtom("C3");
		oBioPolymer.addAtom(oAtom1, oMono1, oStrand1);
		oBioPolymer.addAtom(oAtom2, oMono1, oStrand1);
		oBioPolymer.addAtom(oAtom3, oMono2, oStrand2);

		Assert.assertEquals(oMono1, oBioPolymer.getMonomer("TRP279", "A"));
		Assert.assertEquals(oMono2, oBioPolymer.getMonomer("HOH", "B"));
	}
    
	@Test public void testAddAtom_IAtom() {
	    IBioPolymer oBioPolymer = getBuilder().newBioPolymer();
		
		IAtom oAtom1 = getBuilder().newAtom("C1");
		IAtom oAtom2 = getBuilder().newAtom("C2");
		oBioPolymer.addAtom(oAtom1);
		oBioPolymer.addAtom(oAtom2);

		Assert.assertEquals(2, oBioPolymer.getAtomCount());
	}
    
	@Test public void testAddAtom_IAtom_IStrand() {
	    IBioPolymer oBioPolymer = getBuilder().newBioPolymer();
	    IStrand oStrand1 = getBuilder().newStrand();
		oStrand1.setStrandName("A");
		IMonomer oMono1 = getBuilder().newMonomer();
		oMono1.setMonomerName(new String("TRP279"));
		IAtom oAtom1 = getBuilder().newAtom("C1");
		IAtom oAtom2 = getBuilder().newAtom("C2");
		IAtom oAtom3 = getBuilder().newAtom("C3");
		oBioPolymer.addAtom(oAtom1, oStrand1);
		oBioPolymer.addAtom(oAtom2, oStrand1);
		oBioPolymer.addAtom(oAtom3, oMono1, oStrand1);

		Assert.assertEquals(2, oBioPolymer.getMonomer("", "A").getAtomCount());
		Assert.assertEquals(1, oBioPolymer.getMonomer("TRP279", "A").getAtomCount());
		Assert.assertEquals(3, oBioPolymer.getAtomCount());
	}
	
	@Test public void testAddAtom_IAtom_IMonomer_IStrand()	{
	    IBioPolymer oBioPolymer = getBuilder().newBioPolymer();
	    IStrand oStrand1 = getBuilder().newStrand();
		oStrand1.setStrandName("A");
		IMonomer oMono1 = getBuilder().newMonomer();
		oMono1.setMonomerName(new String("TRP279"));
		IAtom oAtom1 = getBuilder().newAtom("C1");
		IAtom oAtom2 = getBuilder().newAtom("C2");
		oBioPolymer.addAtom(oAtom1, oMono1, oStrand1);
		oBioPolymer.addAtom(oAtom2, oMono1, oStrand1);
		oBioPolymer.addAtom(oAtom1, null, oStrand1);
		
		Assert.assertEquals(2, oBioPolymer.getMonomer("TRP279", "A").getAtomCount());
		Assert.assertEquals(0, oBioPolymer.getMonomer("", "A").getAtomCount());
	}
	
	@Test public void testGetStrandCount()	{
	    IBioPolymer oBioPolymer = getBuilder().newBioPolymer();
	    IStrand oStrand1 = getBuilder().newStrand();
		oStrand1.setStrandName("A");
		IMonomer oMono1 = getBuilder().newMonomer();
		oMono1.setMonomerName(new String("TRP279"));
		IAtom oAtom1 = getBuilder().newAtom("C1");
		oBioPolymer.addAtom(oAtom1, oMono1, oStrand1);

		Assert.assertEquals(1, oBioPolymer.getStrandCount());
	}
	
	@Test public void testGetStrand_String()	{
	    IBioPolymer oBioPolymer = getBuilder().newBioPolymer();
	    IStrand oStrand1 = getBuilder().newStrand();
		oStrand1.setStrandName("A");
		IMonomer oMono1 = getBuilder().newMonomer();
		oMono1.setMonomerName(new String("TRP279"));
		IAtom oAtom1 = getBuilder().newAtom("C1");
		oBioPolymer.addAtom(oAtom1, oMono1, oStrand1);
		
		Assert.assertEquals(oStrand1, oBioPolymer.getStrand("A"));
	}
	
	@Test public void testGetStrandNames()	{
	    IBioPolymer oBioPolymer = getBuilder().newBioPolymer();
	    IStrand oStrand1 = getBuilder().newStrand();
		IStrand oStrand2 = getBuilder().newStrand();
		oStrand1.setStrandName("A");
		oStrand2.setStrandName("B");
		IMonomer oMono1 = getBuilder().newMonomer();
		oMono1.setMonomerName(new String("TRP279"));
		IMonomer oMono2 = getBuilder().newMonomer();
		oMono2.setMonomerName(new String("GLY123"));
		IAtom oAtom1 = getBuilder().newAtom("C1");
		IAtom oAtom2 = getBuilder().newAtom("C2");
		oBioPolymer.addAtom(oAtom1, oMono1, oStrand1);
		oBioPolymer.addAtom(oAtom2, oMono2, oStrand2);
		Map strands = new Hashtable();
		strands.put("A", oStrand1);
		strands.put("B", oStrand2);
		
		Assert.assertEquals(strands.keySet(), oBioPolymer.getStrandNames());
	}
	
	@Test public void testRemoveStrand_String()	{
	    IBioPolymer oBioPolymer = getBuilder().newBioPolymer();
	    IStrand oStrand1 = getBuilder().newStrand();
		oStrand1.setStrandName("A");
		IMonomer oMono1 = getBuilder().newMonomer();
		oMono1.setMonomerName(new String("TRP279"));
		IAtom oAtom1 = getBuilder().newAtom("C1");
		oBioPolymer.addAtom(oAtom1, oMono1, oStrand1);
		
		Assert.assertTrue(oBioPolymer.getStrandNames().contains(oStrand1.getStrandName()));
		Assert.assertEquals(1, oBioPolymer.getAtomCount());
		oBioPolymer.removeStrand("A");
		Assert.assertFalse(oBioPolymer.getStrandNames().contains(oStrand1.getStrandName()));
		Assert.assertEquals(0, oBioPolymer.getAtomCount());
	}
	
	@Test public void testGetStrands()	{
	    IBioPolymer oBioPolymer = getBuilder().newBioPolymer();
	    IStrand oStrand1 = getBuilder().newStrand();
		IStrand oStrand2 = getBuilder().newStrand();
		oStrand1.setStrandName("A");
		oStrand2.setStrandName("B");
		IMonomer oMono1 = getBuilder().newMonomer();
		oMono1.setMonomerName(new String("TRP279"));
		IMonomer oMono2 = getBuilder().newMonomer();
		oMono2.setMonomerName(new String("GLY123"));
		IAtom oAtom1 = getBuilder().newAtom("C1");
		IAtom oAtom2 = getBuilder().newAtom("C2");
		oBioPolymer.addAtom(oAtom1, oMono1, oStrand1);
		oBioPolymer.addAtom(oAtom2, oMono2, oStrand2);
		Map strands = new Hashtable();
		strands.put("A", oStrand1);
		strands.put("B", oStrand2);
		
		Assert.assertEquals(strands, oBioPolymer.getStrands());
	}
    
    /**
     * Method to test whether the class complies with RFC #9.
     */
    @Test public void testToString() {
        IBioPolymer bp = getBuilder().newBioPolymer();
        String description = bp.toString();
        for (int i=0; i< description.length(); i++) {
            Assert.assertTrue('\n' != description.charAt(i));
            Assert.assertTrue('\r' != description.charAt(i));
        }
    }
    
    /**
     * Method to test the clone() method
     */
    @Test public void testClone() throws Exception {
        IBioPolymer polymer = getBuilder().newBioPolymer();
        Object clone = polymer.clone();
        Assert.assertTrue(clone instanceof IBioPolymer);
    }

}
