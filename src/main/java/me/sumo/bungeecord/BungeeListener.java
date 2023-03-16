package me.sumo.bungeecord;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import me.sumo.Main;

public class BungeeListener implements PluginMessageListener {

	@Override
	public void onPluginMessageReceived(String channel, Player arg1, byte[] message) {
		
		if (!channel.equals("BungeeCord")) {
			return;
		}
		
		ByteArrayDataInput input = ByteStreams.newDataInput(message);
		String subchannel = input.readUTF();
		
		if(subchannel.equals("Connect")) {
			return;
		}
	}
	
	public void connectToServer(Player p, String server) {
		ByteArrayDataOutput output = ByteStreams.newDataOutput();
		output.writeUTF("Connect");
		output.writeUTF(server);
		
		p.sendPluginMessage(Main.getInstance(), "BungeeCord", output.toByteArray());
	}
	
	public void broadcastMessage(Player p, String message) {
		ByteArrayDataOutput output = ByteStreams.newDataOutput();
		output.writeUTF("Message");
		output.writeUTF("ALL");
		output.writeUTF(message);
		
		p.sendPluginMessage(Main.getInstance(), "BungeeCord", output.toByteArray());
	}

}
