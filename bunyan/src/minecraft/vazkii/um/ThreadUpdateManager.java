package vazkii.um;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.Minecraft;

/**
 * @author Vazkii
 */
public class ThreadUpdateManager extends Thread {
	
	final Minecraft mc;
	boolean firstTick;
	
	boolean firstTickUpdate;
	
	public ThreadUpdateManager(Minecraft mc){
		setName("Update Manager Thread");
		this.mc = mc;
		firstTick = true;
		setDaemon(true);
		if(UpdateManager.initThread(this))
			start();
		else
			try {
				finalize();
			} catch (Throwable e) {
				e.printStackTrace();
			}
	}
	
	public void run(){
				try {
					while(mc.running){
						int sleepTime = Settings.getInt("checkDelay");
						
						if(sleepTime <= 0){
							System.out.println("[Mod Update Manager] Killing Thread");
							finalize();
						}
						
						while(mc.thePlayer == null ||  mc.theWorld == null || (mc.theWorld.isRemote && !Settings.getBoolean("smpEnable"))) sleep(1000);
						System.out.println("[Mod Update Manager] Thread executed check.");
						String lang = mc.gameSettings.language;
						if(firstTick)
							if(UpdateManager.online){
								if(firstTickUpdate = UpdateManager.areModsUpdated())
								mc.thePlayer.addChatMessage(UpdateManager.areModsUpdated() ? UpdateManagerFacade.localize("um.updated", lang) : UpdateManagerFacade.localize("um.outdated", lang));
								else
									mc.thePlayer.addChatMessage(UpdateManagerFacade.localize("um.outdated", lang));
							}else mc.thePlayer.addChatMessage(UpdateManagerFacade.localize("um.offline", lang));
						else {
							UpdateManager.loadMods();
							if(!UpdateManager.areModsUpdated() && firstTickUpdate)
								mc.thePlayer.addChatMessage(UpdateManagerFacade.localize("um.outdated", lang));
						}
						
						firstTick = false;
						System.out.println(sleepTime);
						sleep(sleepTime*1000);
					}
				} catch (Throwable e) {
					e.printStackTrace();
				}
	}
}
