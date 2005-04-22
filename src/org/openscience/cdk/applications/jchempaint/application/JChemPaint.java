/*
 *  $RCSfile$
 *  $Author$
 *  $Date$
 *  $Revision$
 *
 *  Copyright (C) 1997-2005  The JChemPaint project
 *
 *  Contact: jchempaint-devel@lists.sourceforge.net
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
 */
package org.openscience.jchempaint.application;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.net.URL;
import java.io.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.JInternalFrame.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import org.openscience.cdk.*;
import org.openscience.cdk.applications.plugin.*;
import org.openscience.cdk.controller.*;
import org.openscience.cdk.event.ChemObjectChangeEvent;
import org.openscience.cdk.geometry.GeometryTools;
import org.openscience.cdk.io.*;
import org.openscience.cdk.io.listener.SwingGUIListener;
import org.openscience.cdk.tools.LoggingTool;
import org.openscience.cdk.tools.manipulator.ChemModelManipulator;
import org.openscience.jchempaint.*;
import org.openscience.jchempaint.io.*;
import org.openscience.jchempaint.action.*;
import org.openscience.jchempaint.dialogs.*;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.UnrecognizedOptionException;

/**
 *  JChemPaint main class.
 *
 * @cdk.module jchempaint
 * @author     steinbeck
 * @author     egonw
 * @created    a long time ago
 */
public class JChemPaint extends JFrame implements SwingConstants
{

	private static JChemPaint jchempaintInstance = null;

	/**
	 *  Description of the Field
	 */
	public final static String globalPluginDir = "";

	/**
	 *  Description of the Field
	 */
	public static boolean standAlone = false;

	/**
	 *  Description of the Field
	 */
	public final static String tipSuffix = "Tooltip";

	/**
	 *  Description of the Field
	 */
	public static boolean debug = false;

	/**
	 *  Description of the Field
	 */
	public JButton selectButton;

	/**
	 *  Description of the Field
	 */
	protected FileDialog fileDialog;

	/**
	 *  Description of the Field
	 */
	public static File currentWorkDirectory = null;
	/**
	 *  Description of the Field
	 */
	public static javax.swing.filechooser.FileFilter currentOpenFileFilter = null;
	/**
	 *  Description of the Field
	 */
	public static javax.swing.filechooser.FileFilter currentSaveFileFilter = null;
	/**
	 *  Description of the Field
	 */
	public static File lastOpenedFile = null;
	/**
	 *  Description of the Field
	 */
	public static File lastSavedFile = null;

	private Locale currentLocale = new Locale("en", "EN");

	private static JChemPaintFrame frame;

	/**
	 *  Description of the Field
	 */
	protected CDKPluginManager pluginManager = null;

	/*
	 *  End of GUI declarations
	 */
	/**
	 *  Description of the Field
	 */
	public static File jhome = null;
	/**
	 *  Description of the Field
	 */
	public static File curdir;
	/**
	 *  Description of the Field
	 */
	public static URL jcplogo;
	/**
	 *  This is used for counting the jcp intances in order to be able to close the
	 *  whole vm when the last window of a sdi application is closed
	 */
	private static int jcpcounter = 0;

	private LoggingTool logger;

	boolean embedded = false;


	/**
	 *  The main program for the JChemPaint class
	 *
	 *@param  args  The command line arguments
	 */
	public static void main(String[] args)
	{
		try
		{
			String vers = System.getProperty("java.version");
			String requiredJVM = "1.4.0";
			if (vers.compareTo(requiredJVM) < 0)
			{
				System.err.println("WARNING: JChemPaint 2.0 must be run with a Java VM version " +
						requiredJVM + " or higher.");
				System.err.println("Your JVM version: " + vers);
				System.exit(1);
			}

			Options options = new Options();
			options.addOption("h", "help", false, "give this help page");
			options.addOption("v", "version", false, "gives JChemPaints version number");
			options.addOption("1", "sdi", false, "starts JChemPaint with an Single Document Interface");
			options.addOption(
					OptionBuilder.withArgName("property=value").
					hasArg().
					withValueSeparator().
					withDescription("supported options are given below").
					create("D")
					);

			CommandLine line = null;
			try
			{
				CommandLineParser parser = new PosixParser();
				line = parser.parse(options, args);
			} catch (UnrecognizedOptionException exception)
			{
				System.err.println(exception.getMessage());
				System.exit(-1);
			} catch (ParseException exception)
			{
				System.err.println("Unexpected exception: " + exception.toString());
			}

			if (line.hasOption("v"))
			{
				Package self = Package.getPackage("org.openscience.jchempaint");
				String version = self.getImplementationVersion();

				System.out.println("JChemPaint v." + version + "\n");
				System.exit(0);
			}

			if (line.hasOption("h"))
			{
				Package self = Package.getPackage("org.openscience.jchempaint");
				String version = self.getImplementationVersion();

				System.out.println("JChemPaint v." + version + "\n");

				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("JChemPaint", options);

				// now report on the -D options
				System.out.println();
				System.out.println("The -D options are as follows (defaults in parathesis):");
				System.out.println("  cdk.debugging     [true|false] (false)");
				System.out.println("  cdk.debug.stdout  [true|false] (false)");
				System.out.println("  devel.gui         [true|false] (false)");
				System.out.println("  gui               [stable|experimental] (stable)");
				System.out.println("  plugin.dir        (unset)");
				System.out.println("  user.language     [DE|EN|NL|PL] (EN)");

				System.exit(0);
			}

			// Process command line arguments
			String modelFilename = "";
			FileReader contentToOpen = null;
			args = line.getArgs();
			if (args.length > 0)
			{
				modelFilename = args[0];
				File file = new File(modelFilename);
				if (!file.exists())
				{
					System.err.println("File does not exist: " + modelFilename);
					System.exit(-1);
				}
				// ok, file exists
				contentToOpen = new FileReader(file);
			}

			JChemPaint jcp = JChemPaint.getInstance();

			if (contentToOpen != null)
			{
				jcp.getCurrentFrame().getJChemPaintPanel().showChemFile(contentToOpen);
			}
		} catch (Throwable t)
		{
			System.err.println("uncaught exception: " + t);
			t.printStackTrace(System.err);
		}
	}


	/**
	 *  Constructor for the JChemPaint object
	 */
	private JChemPaint()
	{

		String locale = null;

		// configure Logger
		logger = new LoggingTool(this);
		logger.dumpSystemProperties();

		logger.debug(" ++++ ++++ ++++ ++++ ++++ ++++ ++++ ");
		loadResources();
		logger.debug(" ++++ ++++ ++++ ++++ ++++ ++++ ");
		setupUserLanguage(locale);
		logger.debug(" ++++ ++++ ++++ ++++ ++++ ");
		JChemPaintFrame jcpf = getEmptyFrameWithModel();
		addAndShowJChemPaintFrame(jcpf);
		logger.debug(" ++++ ++++ ++++ ++++ ");
		/*
		 *  if (addPluginMenu) {
		 *  setupPluginManager(jcpf);
		 *  }
		 */
		logger.debug(" ++++ ++++ ++++ ");
		setupWorkingDirectory();
		logger.debug(" ++++ ++++ ");

		logger.debug("End of JCP constructor");
	}


	/**
	 *  Description of the Method
	 */
	private void setupWorkingDirectory()
	{
		try
		{
			if (System.getProperty("user.dir") != null)
			{
				currentWorkDirectory = new File(System.getProperty("user.dir"));
			}
		} catch (Exception exc)
		{
			logger.error("Could not read a system property. I might be in a sandbox.");
		}
	}


	/**
	 *  Description of the Method
	 *
	 *@param  localeString  Description of the Parameter
	 */
	private void setupUserLanguage(String localeString)
	{
		currentLocale = new Locale("en");
		try
		{
			if (localeString == null)
			{
				localeString = System.getProperty("user.language");
			}
			// Set the prefered language {{{
			logger.info("User set language: ", localeString);
			if (localeString != null)
			{
				StringTokenizer st = new StringTokenizer(localeString, "_");
				if (st.hasMoreTokens())
				{
					String language = st.nextToken();
					if (st.hasMoreTokens())
					{
						String country = st.nextToken();
						currentLocale = new Locale(language, country);
					} else
					{
						currentLocale = new Locale(language);
					}
				}
			}
		} catch (Exception exc)
		{
			logger.error("Could not read a system property. I might be in a sandbox.");
			logger.debug(exc);
		}

	}


	/**
	 *  Description of the Method
	 *
	 *@param  jcpf  Description of the Parameter
	 */
	private void setupPluginManager(JChemPaintFrame jcpf)
	{
		try
		{
			// set up plugin manager
			JCPPropertyHandler jcph = JCPPropertyHandler.getInstance();
			pluginManager = new CDKPluginManager(jcph.getJChemPaintDir().toString(), (JChemPaintEditorPanel) jcpf.getJChemPaintPanel());
			// load the plugins that come with JCP itself
			pluginManager.loadPlugin("org.openscience.cdkplugin.dirbrowser.DirBrowserPlugin");
			// load the global plugins
			if (!globalPluginDir.equals(""))
			{
				pluginManager.loadPlugins(globalPluginDir);
			}
			// load the user plugins
			pluginManager.loadPlugins(new File(jcph.getJChemPaintDir(), "plugins").toString());
			// load plugins given with -Dplugin.dir=bla
			if (System.getProperty("plugin.dir") != null)
			{
				pluginManager.loadPlugins(System.getProperty("plugin.dir"));
			}
		} catch (Exception exc)
		{
			logger.error("Could not initialize Plugin-Manager. I might be in a sandbox.");
			logger.debug(exc);
		}
	}


	/**
	 *  Creates a new JChemPaintFrame that owns a new JChemPaintModel and returns
	 *  it. Use addAndShowJChemPaintFrame to actually add it to the desktopPane
	 *
	 *@return    The new JChemPaintFrame with its new JChemPaintModel
	 */
	public JChemPaintFrame getEmptyFrameWithModel()
	{
		JChemPaintModel jcpm = new JChemPaintModel();
		jcpm.setTitle(getNewFrameName());
		jcpm.setAuthor(JCPPropertyHandler.getInstance().getJCPProperties().getProperty("General.UserName"));
		Package self = Package.getPackage("org.openscience.jchempaint");
		String version = self.getImplementationVersion();
		jcpm.setSoftware("JChemPaint " + version);
		jcpm.setGendate((Calendar.getInstance()).getTime().toString());
		JChemPaintFrame jcpf = getNewFrame(jcpm);
		return jcpf;
	}


	/**
	 *  Creates a new localized string that can be used as a title for the new
	 *  frame.
	 *
	 *@return    The newFrameName value
	 */
	private String getNewFrameName()
	{
		return JCPLocalizationHandler.getInstance().getString("Untitled-") + Integer.toString(1);
	}


	/**
	 *  Creates a new JChemPaintFrame and assigns a given Model to it. Use
	 *  addAndShowJChemPaintFrame to actually add it to the desktopPane
	 *
	 *@param  jcpm  The model to be assigned to the new frame.
	 *@return       The new JChemPaintFrame with its new JChemPaintModel
	 */
	public JChemPaintFrame getNewFrame(JChemPaintModel jcpm)
	{
		JChemPaintFrame jcpf = new JChemPaintFrame(jcpm);
		jcpm.addChangeListener(jcpf);
		return jcpf;
	}


	/**
	 *  Adds a given JChemPaintFrame to the central desktopPane of JChemPaint and
	 *  configures it.
	 *
	 *@param  jcpf  The feature to be added to the AndShowJChemPaintFrame attribute
	 */
	public void addAndShowJChemPaintFrame(JChemPaintFrame jcpf)
	{
		jcpf.show();
	}



	/**
	 *  Returns the JChemPaintModel of the JChemPaintFrame that is active.
	 *
	 *@return    JChemPaintModel
	 */
	public JChemPaintModel getCurrentModel()
	{
		JChemPaintFrame currentFrame = getCurrentFrame();
		JChemPaintModel model = null;
		if (currentFrame != null)
		{
			model = currentFrame.getModel();
		} else
		{
			logger.warn("currentFrame == null!");
		}
		return model;
	}


	/**
	 *  Retrieves the collection of JInternalFrames from JChemPaints JDesktopPane,
	 *  searches for the one that is active and returns it.
	 *
	 *@return    JChemPaintFrame The current JChemPaintFrame
	 */
	public JChemPaintFrame getCurrentFrame()
	{
		return frame;
	}


	/**
	 *  Returns the parent Frame. Needed if you want o open a FileDialog.
	 *
	 *@return    Frame The parent Frame
	 */
	public Frame getFrame()
	{
		for (Container p = getParent(); p != null; p = p.getParent())
		{
			if (p instanceof Frame)
			{
				return (Frame) p;
			}
		}
		return null;
	}


	/**
	 *  Tries to load the resources. If run in an applet, try to fail gracefully.
	 */
	private void loadResources()
	{
		try
		{
			UIManager.setLookAndFeel(
					UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception exception)
		{
			logger.error("Error loading L&F: " + exception);
		}
	}


	/**
	 *  Gets the pluginManager attribute of the JChemPaint object
	 *
	 *@return    The pluginManager value
	 */
	public CDKPluginManager getPluginManager()
	{
		return pluginManager;
	}


	/**
	 *  Description of the Method
	 */
	public void exitJChemPaint()
	{
		jcpcounter--;
		// first shut down the plugins
		// pluginManager.closePlugins();
		// close JVM
		System.exit(0);
	}


	/**
	 *  Action that will close JChemPaint.
	 *
	 *@author     steinbeck
	 *@created    February 18, 2004
	 */
	public final static class AppCloser extends WindowAdapter
	{

		private static JChemPaint jcp;


		/**
		 *  Constructor for the AppCloser object
		 *
		 *@param  jcp  Description of the Parameter
		 */
		public AppCloser(JChemPaint jcp)
		{
			this.jcp = jcp;
		}


		/**
		 *  Terminates the currently running Java Virtual Machine. @ param e Window
		 *  closing Event
		 *
		 *@param  e  Description of the Parameter
		 */
		public void windowClosing(WindowEvent e)
		{
			jcp.exitJChemPaint();
		}
	}



	/**
	 *  Gets the selection attribute of the JChemPaint object
	 *
	 *@return    The selection value
	 */
	public AtomContainer getSelection()
	{
		JChemPaintFrame frame = getCurrentFrame();
		if (frame == null || frame.getModel() == null ||
				frame.getModel().getRendererModel() == null)
		{
			return null;
		}
		return (frame.getModel().getRendererModel().getSelectedPart());
	}


	/**
	 *  Gets the instance attribute of the JChemPaint class
	 *
	 *@return    The instance value
	 */
	public static JChemPaint getInstance()
	{
		if (jchempaintInstance == null)
		{
			jchempaintInstance = new JChemPaint();
		}
		return jchempaintInstance;
	}
}

