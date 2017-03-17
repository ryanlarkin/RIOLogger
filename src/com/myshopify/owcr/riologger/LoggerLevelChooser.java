/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

package com.myshopify.owcr.riologger;  
/*
 * TrayIconDemo.java
 */

import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URL;
import java.util.logging.Level;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class LoggerLevelChooser {
    
    private static Level logLevel = Level.OFF;
    private static boolean shouldExit = false;
    
    public static Level getLogLevel() {
        return logLevel;
    }
    
    public static boolean shouldExit() {
        return shouldExit;
    }
    
    public static void setUpTray() {
        /* Use an appropriate Look and Feel */
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            //UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        //Schedule a job for the event-dispatching thread:
        //adding TrayIcon.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
    
    private static void createAndShowGUI() {
        //Check the SystemTray support
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
        final PopupMenu popup = new PopupMenu();
        final TrayIcon trayIcon =
                new TrayIcon(createImage("/images/icon.png", "tray icon"));
        final SystemTray tray = SystemTray.getSystemTray();
        
        // Create a popup menu components
        MenuItem aboutItem = new MenuItem("About");
        Menu displayMenu = new Menu("Log Fineness");
        
        CheckboxMenuItem noneItem = new CheckboxMenuItem("None", true);
        CheckboxMenuItem allItem = new CheckboxMenuItem("All", false);
        CheckboxMenuItem debugItem = new CheckboxMenuItem("Debug", false);
        CheckboxMenuItem infoItem = new CheckboxMenuItem("Info", false);
        CheckboxMenuItem warningItem = new CheckboxMenuItem("Warning", false);
        CheckboxMenuItem errorItem = new CheckboxMenuItem("Error", false);
        MenuItem exitItem = new MenuItem("Exit");
        
        //Add components to popup menu
        popup.add(aboutItem);
        popup.addSeparator();
        
        popup.add(displayMenu);
        displayMenu.add(noneItem);
        displayMenu.add(allItem);
        displayMenu.add(debugItem);
        displayMenu.add(infoItem);
        displayMenu.add(warningItem);
        displayMenu.add(errorItem);
        popup.add(exitItem);
        
        trayIcon.setPopupMenu(popup);
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("RIOLogger");
        
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
            return;
        }
        
        trayIcon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "This dialog box is run from System Tray");
            }
        });
        
        aboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "This dialog box is run from the About menu item");
            }
        });
        
        ItemListener listener = new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                CheckboxMenuItem item = (CheckboxMenuItem)e.getSource();
                //TrayIcon.MessageType type = null;
                if(item.getLabel().equals("None")) {
                    noneItem.setState(true);
                    allItem.setState(false);
                    debugItem.setState(false);
                    infoItem.setState(false);
                    warningItem.setState(false);
                    errorItem.setState(false);
                    
                    logLevel = Level.OFF;
                }
                else if(item.getLabel().equals("All")) {
                    noneItem.setState(false);
                    allItem.setState(true);
                    debugItem.setState(true);
                    infoItem.setState(true);
                    warningItem.setState(true);
                    errorItem.setState(true);
                    
                    logLevel = Level.ALL;
                }
                else if(item.getLabel().equals("Debug")) {
                    noneItem.setState(false);
                    allItem.setState(false);
                    debugItem.setState(true);
                    infoItem.setState(true);
                    warningItem.setState(true);
                    errorItem.setState(true);
                    
                    logLevel = Level.CONFIG;
                }
                else if(item.getLabel().equals("Info")) {
                    noneItem.setState(false);
                    allItem.setState(false);
                    debugItem.setState(false);
                    infoItem.setState(true);
                    warningItem.setState(true);
                    errorItem.setState(true);
                    
                    logLevel = Level.INFO;
                }
                else if(item.getLabel().equals("Warning")) {
                    noneItem.setState(false);
                    allItem.setState(false);
                    debugItem.setState(false);
                    infoItem.setState(false);
                    warningItem.setState(true);
                    errorItem.setState(true);
                    
                    logLevel = Level.WARNING;
                }
                else if(item.getLabel().equals("Error")) {
                    noneItem.setState(false);
                    allItem.setState(false);
                    debugItem.setState(false);
                    infoItem.setState(false);
                    warningItem.setState(false);
                    errorItem.setState(true);
                    
                    logLevel = Level.SEVERE;
                }
            }
        };
        
        noneItem.addItemListener(listener);
        allItem.addItemListener(listener);
        debugItem.addItemListener(listener);
        infoItem.addItemListener(listener);
        warningItem.addItemListener(listener);
        errorItem.addItemListener(listener);
        
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tray.remove(trayIcon);
                shouldExit = true;
            }
        });
    }
    
    //Obtain the image URL
    protected static Image createImage(String path, String description) {
        URL imageURL = TrayIcon.class.getResource(path);
        
        if (imageURL == null) {
            System.err.println("Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }
}