/*
 *  $RCSfile$
 *  $Author$
 *  $Date$
 *  $Revision$
 *
 *  Copyright (C) 1997-2002  The Chemistry Development Kit (CDK) project
 *
 *  Contact: steinbeck@ice.mpg.de, gezelter@maul.chem.nd.edu, egonw@sci.kun.nl
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2.1
 *  of the License, or (at your option) any later version.
 *  All we ask is that proper credit is given for our work, which includes
 *  - but is not limited to - adding the above copyright notice to the beginning
 *  of your source code files, and to any copyright notice that you may distribute
 *  with programs based on this work.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 */
package org.openscience.cdk.tools;

import org.openscience.cdk.*;
import java.util.Vector;
import java.io.*;

/**
 *  Provides methods for checking whether an atoms valences are saturated with
 *  respect to a particular atom type
 *
 *@author     steinbeck
 *@created    September 4, 2001
 *@keyword    saturation
 *@keyword    atom, valency
 */
public class SaturationChecker
{

	AtomTypeFactory atf;

	private org.openscience.cdk.tools.LoggingTool logger;


	/**
	 *  Constructor for the SaturationChecker object
	 *
	 *@exception  java.lang.Exception  Description of the Exception
	 */
	public SaturationChecker() throws java.lang.Exception
	{
		atf = new AtomTypeFactory();
		logger = new org.openscience.cdk.tools.LoggingTool(this.getClass().getName());
	}


	/**
	 *  Description of the Method
	 *
	 *@param  atom  Description of the Parameter
	 *@param  ac    Description of the Parameter
	 *@return       Description of the Return Value
	 */
	public boolean hasPerfectConfiguration(Atom atom, AtomContainer ac)
	{

		double bondOrderSum = ac.getBondOrderSum(atom);
		double maxBondOrder = ac.getHighestCurrentBondOrder(atom);
		AtomType[] atomTypes = atf.getAtomTypes(atom.getSymbol(), atf.ATOMTYPE_ID_STRUCTGEN);
		logger.debug("*** Checking for perfect configuration ***");
		try
		{
			logger.debug("Checking configuration of atom " + ac.getAtomNumber(atom));
			logger.debug("Atom has bondOrderSum = " + bondOrderSum);
			logger.debug("Atom has max = " + bondOrderSum);
		} catch (Exception exc)
		{
		}
		for (int f = 0; f < atomTypes.length; f++)
		{
			if (bondOrderSum == atomTypes[f].getMaxBondOrderSum() && maxBondOrder == atomTypes[f].getMaxBondOrder())
			{
				try
				{
					logger.debug("Atom " + ac.getAtomNumber(atom) + " has perfect configuration");
				} catch (Exception exc)
				{
				}
				return true;
			}
		}
		try
		{
			logger.debug("*** Atom " + ac.getAtomNumber(atom) + " has imperfect configuration ***");
		} catch (Exception exc)
		{
		}
		return false;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  ac  Description of the Parameter
	 *@return     Description of the Return Value
	 */
	public boolean allSaturated(AtomContainer ac)
	{
		for (int f = 0; f < ac.getAtomCount(); f++)
		{
			if (!isSaturated(ac.getAtomAt(f), ac))
			{
				return false;
			}
		}
		return true;
	}


	/**
	 *  Gets the saturated attribute of the SaturationChecker object
	 *
	 *@param  atom  Description of the Parameter
	 *@param  ac    Description of the Parameter
	 *@return       The saturated value
	 */
	public boolean isSaturated(Atom atom, AtomContainer ac)
	{
		AtomType[] atomTypes = atf.getAtomTypes(atom.getSymbol(), atf.ATOMTYPE_ID_STRUCTGEN);
		double bondOrderSum = ac.getBondOrderSum(atom);
		double maxBondOrder = ac.getHighestCurrentBondOrder(atom);
		int hcount = atom.getHydrogenCount();
		try
		{
			logger.debug("*** Checking saturation of atom " + ac.getAtomNumber(atom) + " ***");
			logger.debug("bondOrderSum: " + bondOrderSum);
			logger.debug("maxBondOrder: " + maxBondOrder);
			logger.debug("hcount: " + hcount);
		} catch (Exception exc)
		{
		}
		for (int f = 0; f < atomTypes.length; f++)
		{
			if (bondOrderSum >= atomTypes[f].getMaxBondOrderSum() - hcount && maxBondOrder <= atomTypes[f].getMaxBondOrder())
			{
				logger.debug("*** Good ! ***");
				return true;
			}
		}
		logger.debug("*** Bad ! ***");
		return false;
	}

	/**
	 *  Checks if the current atom has exceeded its bond order sum value
	 *
	 *@param  atom The Atom to check
	 *@param  ac   The atomcontainer context
	 *@return      oversaturated or not
	 */
	public boolean isOverSaturated(Atom atom, AtomContainer ac)
	{
		AtomType[] atomTypes = atf.getAtomTypes(atom.getSymbol(), atf.ATOMTYPE_ID_STRUCTGEN);
		double bondOrderSum = ac.getBondOrderSum(atom);
		double maxBondOrder = ac.getHighestCurrentBondOrder(atom);
		int hcount = atom.getHydrogenCount();
		try
		{
			logger.debug("*** Checking saturation of atom " + ac.getAtomNumber(atom) + " ***");
			logger.debug("bondOrderSum: " + bondOrderSum);
			logger.debug("maxBondOrder: " + maxBondOrder);
			logger.debug("hcount: " + hcount);
		} catch (Exception exc)
		{
		}
		for (int f = 0; f < atomTypes.length; f++)
		{
			if (bondOrderSum > atomTypes[f].getMaxBondOrderSum() - hcount)
			{
				logger.debug("*** Good ! ***");
				return true;
			}
		}
		logger.debug("*** Bad ! ***");
		return false;
	}
	/**
	 *  Returns the currently maximum formable bond order for this atom
	 *
	 *@param  atom  The atom to be checked
	 *@param  ac    The AtomContainer that provides the context
	 *@return       the currently maximum formable bond order for this atom
	 */
	public double getCurrentMaxBondOrder(Atom atom, AtomContainer ac)
	{
		AtomType[] atomTypes = atf.getAtomTypes(atom.getSymbol(), atf.ATOMTYPE_ID_STRUCTGEN);
		double bondOrderSum = ac.getBondOrderSum(atom);
		double maxBondOrder = ac.getHighestCurrentBondOrder(atom);
		int hcount = atom.getHydrogenCount();
		double max = 0;
		double current = 0;
		for (int f = 0; f < atomTypes.length; f++)
		{
			current = hcount + bondOrderSum;
			if (atomTypes[f].getMaxBondOrderSum() - current > max)
			{
				max = atomTypes[f].getMaxBondOrderSum() - current;
			}
		}
		return max;
	}


	/**
	 *  Saturates a molecule by setting appropriate bond orders.
	 *
	 *@param  molecule  Description of the Parameter
	 *@keyword          bond order, calculation
	 */
	public void saturate(Molecule molecule)
	{
		Atom partner = null;
		Atom atom = null;
		Atom[] partners = null;
		AtomType[] atomTypes1 = null;
		AtomType[] atomTypes2 = null;
		Bond bond = null;
		for (int i = 1; i < 4; i++)
		{
			// handle atoms with degree 1 first and then proceed to higher order
			for (int f = 0; f < molecule.getAtomCount(); f++)
			{
				atom = molecule.getAtomAt(f);
				System.out.println(atom.getSymbol());
				atomTypes1 = atf.getAtomTypes(atom.getSymbol(), atf.ATOMTYPE_ID_STRUCTGEN);
				System.out.println(atomTypes1[0]);
				if (molecule.getBondCount(atom) == i)
				{
					if (molecule.getBondOrderSum(atom) < atomTypes1[0].getMaxBondOrderSum() - atom.getHydrogenCount())
					{
						partners = molecule.getConnectedAtoms(atom);
						for (int g = 0; g < partners.length; g++)
						{
							partner = partners[g];
							atomTypes2 = atf.getAtomTypes(partner.getSymbol(), atf.ATOMTYPE_ID_STRUCTGEN);

							if (molecule.getBondOrderSum(partner) < atomTypes2[0].getMaxBondOrderSum() - partner.getHydrogenCount())
							{
								bond = molecule.getBond(atom, partner);
								bond.setOrder(bond.getOrder() + 1);
								break;
							}
						}
					}
				}
			}
		}
	}


	/**
	 *  Recursivly fixes bond orders in a molecule for 
	 *  which only connectivities but no bond orders are know.
	 *
	 *@param  molecule  The molecule to fix the bond orders for
	 *@param  bond      The number of the bond to treat in this recursion step
	 *@return           true if the bond order which was implemented was ok.
	 */
	/*private boolean recursiveBondOrderFix(Molecule molecule, int bondNumber)
	{	

		Atom partner = null;
		Atom atom = null;
		Atom[] partners = null;
		AtomType[] atomTypes1 = null;
		AtomType[] atomTypes2 = null;
		int maxBondOrder = 0;
		int oldBondOrder = 0;
		if (bondNumber < molecule.getBondCount())
		{	
			Bond bond = molecule.getBondAt(f);
		}
		else 
		{
			return true;
		}
		atom = bond.getAtomAt(0);
		partner = bond.getAtomAt(1);
		atomTypes1 = atf.getAtomTypes(atom.getSymbol(), atf.ATOMTYPE_ID_STRUCTGEN);
		atomTypes2 = atf.getAtomTypes(partner.getSymbol(), atf.ATOMTYPE_ID_STRUCTGEN);
		maxBondOrder = Math.min(atomTypes1[0].getMaxBondOrder(), atomTypes2[0].getMaxBondOrder());
		for (int f = 1; f <= maxBondOrder; f++)
		{
			oldBondOrder = bond.getOrder()
			bond.setOrder(f);
			if (!isOverSaturated(atom, molecule) && !isOverSaturated(partner, molecule))
			{
				if (!recursiveBondOrderFix(molecule, bondNumber + 1)) break;
					
			}
			else
			{
				bond.setOrder(oldBondOrder);
				return false;	
			}
		}
		return true;
	}*/


	/**
	 *  Calculate the number of missing hydrogens by substracting the number of
	 *  bonds for the atom from the expected number of bonds. Charges are included
	 *  in the calculation. The number of expected bonds is defined by the AtomType
	 *  generated with the AtomTypeFactory.
	 *
	 *@param  atom      Description of the Parameter
	 *@param  molecule  Description of the Parameter
	 *@return           Description of the Return Value
	 *@see              AtomTypeFactory
	 */
	private int calculateMissingHydrogen(Atom atom, Molecule molecule)
	{
		logger.info("Calculating number of missing hydrogen atoms");
		// get default atom
		AtomType[] atomTypes = atf.getAtomTypes(atom.getSymbol(), atf.ATOMTYPE_ID_STRUCTGEN);
		logger.debug("Found atomtypes: " + atomTypes.length);
		AtomType defaultAtom = atomTypes[0];
		logger.debug("DefAtom: " + defaultAtom.toString());
		int missingHydrogen = (int) (defaultAtom.getMaxBondOrderSum() -
				molecule.getBondOrderSum(atom) +
				atom.getFormalCharge());
		if (atom.flags[CDKConstants.ISAROMATIC]) missingHydrogen--;				
		logger.debug("Atom: " + atom.getSymbol());
		logger.debug("  max bond order: " + defaultAtom.getMaxBondOrderSum());
		logger.debug("  bond order sum: " + molecule.getBondOrderSum(atom));
		logger.debug("  charge        : " + atom.getFormalCharge());
		return missingHydrogen;
	}


	/**
	 *  Method that saturates a molecule by adding explicit hydrogens.
	 *
	 *@param  molecule  Molecule to saturate
	 *@keyword          hydrogen, adding
	 *@keyword          explicit hydrogen
	 */
	public void addHydrogensToSatisfyValency(Molecule molecule)
	{
		Atom[] atoms = molecule.getAtoms();
		for (int f = 0; f < atoms.length; f++)
		{
			Atom atom = atoms[f];
			atom.setHydrogenCount(0);
			// set number of implicit hydrogens to zero
			// add explicit hydrogens
			int missingHydrogens = calculateMissingHydrogen(atom, molecule);
			for (int i = 1; i <= missingHydrogens; i++)
			{
				Atom hydrogen = new Atom("H");
				hydrogen.setPoint2D(atom.getPoint2D());
				molecule.addAtom(hydrogen);
				Bond newBond = new Bond(atom, hydrogen, 1.0);
				molecule.addBond(newBond);
			}
		}
	}


	/**
	 *  Method that saturates a molecule by adding implicit hydrogens.
	 *
	 *@param  molecule  Molecule to saturate
	 *@keyword          hydrogen, adding
	 *@keyword          implicit hydrogen
	 */
	public void addImplicitHydrogensToSatisfyValency(Molecule molecule)
	{
		Atom[] atoms = molecule.getAtoms();
		for (int f = 0; f < atoms.length; f++)
		{
			Atom atom = atoms[f];
			// add implicit hydrogens
			int missingHydrogens = calculateMissingHydrogen(atom, molecule);
			atom.setHydrogenCount(missingHydrogens);
		}
	}
	
	

}

