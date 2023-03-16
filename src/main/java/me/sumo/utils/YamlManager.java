package me.sumo.utils;
 
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import me.sumo.Main;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;


public class YamlManager {
 
	public YamlConfiguration cfg;
	  private Main plugin = Main.getInstance();
	  public File yamlpath;
	  public YamlConfiguration YAML;
	  public String YamlName;

	  public void CreateMConfig(File yamlpath, String YamlName)
	  {
	    if (!this.plugin.getDataFolder().exists()) {
	      this.plugin.getDataFolder().mkdir();
	    }

	    if (!yamlpath.exists()) {
	      try {
	        yamlpath.createNewFile();
	        Bukkit.getServer().getConsoleSender()
	          .sendMessage(ChatColor.YELLOW + "[" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "Sumo" +
	          ChatColor.YELLOW + "] " + ChatColor.GREEN + " The " + YamlName + 
	          " file has been created");
	      } catch (IOException e) {
	        e.printStackTrace();
	        Bukkit.getServer().getConsoleSender()
	          .sendMessage(ChatColor.YELLOW + "[" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "Sumo" +
	          ChatColor.YELLOW + "] " + ChatColor.RED + " Could not create the" + YamlName + 
	          "file");
	      }
	    }

	    this.cfg = YamlConfiguration.loadConfiguration(yamlpath);
	  }

	  public void SetupMConfig(File yamlpath, String YamlName) {
	    if (!this.plugin.getDataFolder().exists()) {
	      this.plugin.getDataFolder().mkdir();
	    }
	    if (!yamlpath.exists())
	    {
	      InputStream in = this.plugin.getClass().getResourceAsStream("/" + YamlName);
	      try {
	        FileOutputStream out = new FileOutputStream(yamlpath);
	        IOUtils.copy(in, out);
	        in.close();
	        out.close();
	        Bukkit.getServer().getConsoleSender()
	          .sendMessage(ChatColor.YELLOW + "[" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "Sumo" +
	          ChatColor.YELLOW + "] " + ChatColor.RED + " Copied Default " + YamlName + "!");
	      } catch (FileNotFoundException e1) {
	        e1.printStackTrace();
	      } catch (IOException e) {
	        e.printStackTrace();
	      }
	    }
	  }

	  public void saveMConfig(File yamlpath, YamlConfiguration Yaml) {
	    try {
	      Yaml.save(yamlpath);
	    } catch (IOException e) {
	      Bukkit.getServer().getConsoleSender()
	        .sendMessage(ChatColor.YELLOW + "[" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "Sumo" +
	        ChatColor.YELLOW + "] " + ChatColor.RED + " Could Not Save The " + this.YamlName + " File");
	    }
	  }

	  public YamlConfiguration GetLoadedYaml(File yamlpath) {
	    return YamlConfiguration.loadConfiguration(yamlpath);
	  }

	  public void reloadMConfig(File yamlpath, String YamlName) {
	    this.cfg = YamlConfiguration.loadConfiguration(yamlpath);
	    Bukkit.getServer().getConsoleSender()
	      .sendMessage(ChatColor.YELLOW + "[" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "Sumo" +
	      ChatColor.YELLOW + "] " + ChatColor.BLUE + " The " + YamlName + " File Has Been loaded");
	  }
	}